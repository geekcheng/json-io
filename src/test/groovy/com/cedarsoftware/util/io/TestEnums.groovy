package com.cedarsoftware.util.io

import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License")
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
class TestEnums
{
    private enum TestEnum1 { A, B, C }

    private enum TestEnum2
    {
        A() {},
        B() {},
        C() {}
    }

    private enum TestEnum3
    {
        A("Foo") {
            void doXX() {}
        },
        B("Bar") {
            void doXX() {}
        },
        C(null) {
            void doXX() {}
        };

        private final String val

        TestEnum3(String val)
        {
            this.val = val;
        }

        abstract void doXX()
    }

    private static enum TestEnum4
    {
        A, B, C;

        private int internal = 6;
        protected long age = 21;
        String foo = "bar"
    }

    @Test
    void testEnums() throws Exception
    {

        Collection<Object> collection = new LinkedList<Object>()
        collection.addAll(Arrays.asList(TestEnum1.values()))
        collection.addAll(Arrays.asList(TestEnum2.values()))
        collection.addAll(Arrays.asList(TestEnum3.values()))

        for (Object o : collection)
        {
            String json = TestUtil.getJsonString(o)
            TestUtil.printLine("json=" + json)
            Object read = TestUtil.readJsonObject(json)
            assertTrue(o == read)
        }

        String json = TestUtil.getJsonString(collection)
        Collection<Object> collection2 = (Collection<Object>) TestUtil.readJsonObject(json)
        assertTrue(collection.equals(collection2))

        TestEnum1[] array11 = TestEnum1.values()
        json = TestUtil.getJsonString(array11)
        TestUtil.printLine("json=" + json)
        TestEnum1[] array12 = (TestEnum1[]) TestUtil.readJsonObject(json)
        assertTrue(Arrays.equals(array11, array12))

        TestEnum2[] array21 = TestEnum2.values()
        json = TestUtil.getJsonString(array21)
        TestUtil.printLine("json=" + json)
        TestEnum2[] array22 = (TestEnum2[]) TestUtil.readJsonObject(json)
        assertTrue(Arrays.equals(array21, array22))

        TestEnum3[] array31 = TestEnum3.values()
        json = TestUtil.getJsonString(array31)
        TestUtil.printLine("json=" + json)
        TestEnum3[] array32 = (TestEnum3[]) TestUtil.readJsonObject(json)
        assertTrue(Arrays.equals(array31, array32))
    }

    @Test
    void testEnumWithPrivateMembersAsField() throws Exception
    {
        TestEnum4 x = TestEnum4.B;
        String json = TestUtil.getJsonString(x)
        TestUtil.printLine(json)
        def className = TestEnum4.class.name
        assert '{"@type":"' + className + '","internal":6,"age":21,"foo":"bar","name":"B","ordinal":1}' == json

        ByteArrayOutputStream ba = new ByteArrayOutputStream()
        JsonWriter writer = new JsonWriter(ba)
        writer.args.put(JsonWriter.ENUM_PUBLIC_ONLY, true)
        writer.write(x)
        json = new String(ba.toByteArray())
        TestUtil.printLine(json)
        assert '{"@type":"' + className + '","name":"B","ordinal":1}' == json
    }
}
