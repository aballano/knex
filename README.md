<img src="./art/web_hi_res_512.png" width="250">

KNex 

[![Build Status](https://travis-ci.org/aballano/knex.svg?branch=master)](https://travis-ci.org/aballano/knex) [![JitPack](https://jitpack.io/v/aballano/knex.svg)](https://jitpack.io/#aballano/knex) [![codecov](https://codecov.io/gh/aballano/knex/branch/master/graph/badge.svg)](https://codecov.io/gh/aballano/knex) 

===

A full Kotlin library to easily build Recyclerview's adapters.


Based on [Renderers lib](https://github.com/pedrovgs/Renderers) made by [pedrovgs](https://github.com/pedrovgs)


DIFFERENCE WITH RENDERERS
---

The main difference of this project is that is totally generic, which means:

* No need to wrap every model in another object unless you want to go for multiple type bindings.
* Possibility to bind more complex objects without extra effort.
* No Renderer cloning: as this might causes issues with several libraries such as Butterknife and others

But be aware that also means that **you'll loose type safety** when adding items to your adapter. On the other hand, you'll have this type safety when declaring the KnexRenderers as shown below.

This is because the list will now be of type Any, thus we cannot ensure on compile time that the items you add have the proper renderer

USAGE
---

First of all, let's create as many KnexRenderer as different views we need, for example:

```kotlin
class VideoKnexRenderer : KnexRenderer<Video>() {

    private val thumbnail: ImageView by bindView(R.id.iv_thumbnail)
    private val title: TextView by bindView(R.id.tv_title)
    private lateinit var imageLoader: RequestManager

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.video_view, parent, false)

    override fun setUpView() {
        imageLoader = Glide.with(context)
        thumbnail.setOnClickListener {
            val video = it.tag as Video
            Toast.makeText(context, "Video clicked. Title = ${video.title}", Toast.LENGTH_LONG).show()
        }
    }

    override fun render(content: Video, position: Int, payloads: List<*>) {
        thumbnail.tag = content
        imageLoader.clear(thumbnail)
        imageLoader.load(content.thumbnail)
            .into(thumbnail)
        title.text = content.title
    }
}
```

Now there are 2 possible usages:

### Simple usage: only 1 item type

What we do is to pass a KnexFactory in the form `() -> KnexRenderer<T>`

```kotlin
val adapter = KnexBuilder.create(::VideoKnexRenderer)
              .build()
              .into(recyclerView)
```

<img src="./art/screenshot_demo_1.jpg?raw=true" width="400">

### Advanced usage: multiple models

Ok, let's assume we now have another KnexRenderer called `SectionKnexRenderer` and `FooterKnexRenderer` 
which are respectively a section separator and a footer for our videos. 

In this example, both are going to be of type String, therefore we need to wrap them into a 
`KnexContent` class, in order not to class with the types:

```kotlin
open class SectionKnexRenderer : KnexRenderer<KnexContent<String>>() {

    private val title: TextView by bindView(R.id.tv_title)

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
          inflater.inflate(R.layout.section_view, parent, false)

    override fun render(content: KnexContent<String>, position: Int, payloads: List<*>) {
        title.text = content.item
    }
}
```
and:
```kotlin
class FooterKnexRenderer : SectionKnexRenderer() {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
          inflater.inflate(R.layout.footer_view, parent, false)
}
```

As you can imagine, `KnexContent` is just a wrapper that contains the item type we want to add and an unique identificator.

Now, in order to bind our new renderers, we do:

```kotlin
private val TYPE_SECTION = 0
private val TYPE_FOOTER = 1

val adapter = KnexBuilder.create()
      .bind(Video::class, ::VideoKnexRenderer)
      // You could also use a normal lambda in case you want to pass constructor params
      .bind(TYPE_FOOTER) { FooterKnexRenderer(/* some params */) }
      .bind(TYPE_SECTION, ::SectionKnexRenderer)
      .build()
      .into(recyclerView)
```

As you can see we use the default create method for the `KnexBuilder` and then use the chained bind methods to specify
the renderer type for each item type we have. 

We now want to add the items dynamically, but since our Footer and Section renderers both manage strings we need to wrap the values:

```kotlin
videoCollection.forEachIndexed { index, video ->
    adapter.add(KnexContent("Video #" + (index + 1), TYPE_SECTION))
    adapter.add(video)
}

adapter.add(KnexContent("by Alberto Ballano", TYPE_FOOTER))
```

Alternatively you could use this beautiful infix operator to make it more explicit:

```kotlin
videoCollection.forEachIndexed { index, video ->
    // Equivalent to: KnexContent("Video #" + (index + 1), TYPE_SECTION)
    adapter.add("Video #" + (index + 1) asContentType TYPE_SECTION)
    adapter.add(video)
}
adapter.add("by Alberto Ballano" asContentType TYPE_FOOTER)
```

As you can see there's no problem in adding different types since the list in the adapter will be of type `Any`. In 
case that you add a different type that doesn't have a Renderer associated with, an exception will be thrown.


<img src="./art/screenshot_demo_2.jpg?raw=true" width="400">

#### EXTRA: Note that binding generic classes is also possible:

Assuming that we have many different Video implementations extending from `BaseVideo` but we want to map all of them to 
the same renderer we could just do:

```kotlin
val adapter = KnexBuilder.create()
      .bind(BaseVideo::class, ::VideoKnexRenderer)
      .bind(TYPE_FOOTER, ::FooterKnexRenderer)
      .bind(TYPE_SECTION, ::SectionKnexRenderer)
      .build()
      .into(recyclerView)
```

And therefore all `BaseVideo` subclasses added to the adapter will be mapped to the `VideoRenderer`. For obvious reasons 
bindings to `Any` is forbidden to avoid unexpected errors, for that case please check the first usage above.

INCLUDING IN YOUR PROJECT
---

With gradle: edit your build.gradle
```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
        compile 'com.github.aballano:knex:1.0'
}
```

Or declare it into your pom.xml

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.aballano</groupId>
    <artifactId>knex</artifactId>
    <version>1.0</version>
</dependency>
```

