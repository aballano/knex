package com.aballano.knex

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class KnexContentTest {
    @Test fun `should return item item`() {
        val item = "string"
        val rendererContent = KnexContent(item, 1)

        assertEquals(item, rendererContent.item)
    }

    @Test fun `should return type`() {
        val type = 1
        val rendererContent = KnexContent("string", type)

        assertEquals(type, rendererContent.type)
    }

    @Test fun `should do equals with item and type`() {
        val rendererContent1 = KnexContent("a", 1)
        val rendererContent2 = KnexContent("b", 1)
        val rendererContent3 = KnexContent("a", 2)
        val rendererContent4 = KnexContent("a", 1)

        assertNotEquals(rendererContent1, rendererContent2)
        assertNotEquals(rendererContent1, rendererContent3)
        assertNotEquals(rendererContent2, rendererContent3)

        assertEquals(rendererContent1, rendererContent4)
    }

    @Test fun `should do hashcode with item and type`() {
        val rendererContent1 = KnexContent("a", 1)
        val rendererContent2 = KnexContent("b", 1)
        val rendererContent3 = KnexContent("a", 2)
        val rendererContent4 = KnexContent("a", 1)

        assertNotEquals(rendererContent1.hashCode(), rendererContent2.hashCode())
        assertNotEquals(rendererContent1.hashCode(), rendererContent3.hashCode())
        assertNotEquals(rendererContent2.hashCode(), rendererContent3.hashCode())

        assertEquals(rendererContent1.hashCode(), rendererContent4.hashCode())
    }
}