/*
 *  Copyright (c) 2001-2023, Jean Tessier
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of Jean Tessier nor the names of his contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jeantessier.classreader.impl;

public class TestModule_attributeWithModuleProvides extends TestAttributeBase {
    private static final int MODULE_NAME_INDEX = 123;
    private static final String MODULE_NAME = "abc";
    private static final int MODULE_FLAGS = 456;
    private static final int MODULE_VERSION_INDEX = 789;
    private static final String MODULE_VERSION = "blah";
    private static final int REQUIRES_COUNT = 0;
    private static final int EXPORTS_COUNT = 0;
    private static final int OPENS_COUNT = 0;
    private static final int USES_COUNT = 0;

    public void testOneOpens() throws Exception {
        // Given
        expectReadAttributeLength(22);
        expectReadU2(MODULE_NAME_INDEX);
        allowingLookupModule(MODULE_NAME_INDEX, MODULE_NAME, "module name lookup during construction");
        expectReadU2(MODULE_FLAGS);
        expectReadU2(MODULE_VERSION_INDEX);
        allowingLookupUtf8(MODULE_VERSION_INDEX, MODULE_VERSION, "module version lookup during construction");
        expectReadU2(REQUIRES_COUNT);
        expectReadU2(EXPORTS_COUNT);
        expectReadU2(OPENS_COUNT);
        expectReadU2(USES_COUNT);

        // And
        var providesCount = 1;
        expectReadU2(providesCount);

        // And
        var providesIndex = 234;
        var className = "Def";
        expectReadU2(providesIndex);
        allowingLookupClass(providesIndex, className, "lookup during construction");
        expectReadU2(0);

        // And
        var sut = new Module_attribute(mockConstantPool, mockOwner, mockIn);

        // When
        var actualProvides = sut.getProvides();

        //Then
        assertEquals("number of provides", providesCount, actualProvides.size());
        assertEquals(
                "opens",
                providesIndex,
                actualProvides.stream()
                        .mapToInt(ModuleProvides::getProvidesIndex)
                        .findFirst()
                        .orElseThrow());
    }

    public void testMultipleOpens() throws Exception {
        // Given
        expectReadAttributeLength(28);
        expectReadU2(MODULE_NAME_INDEX);
        allowingLookupModule(MODULE_NAME_INDEX, MODULE_NAME, "module name lookup during construction");
        expectReadU2(MODULE_FLAGS);
        expectReadU2(MODULE_VERSION_INDEX);
        allowingLookupUtf8(MODULE_VERSION_INDEX, MODULE_VERSION, "module version lookup during construction");
        expectReadU2(REQUIRES_COUNT);
        expectReadU2(EXPORTS_COUNT);
        expectReadU2(OPENS_COUNT);
        expectReadU2(USES_COUNT);

        // And
        var providesCount = 2;
        expectReadU2(providesCount);

        // And
        var providesIndex1 = 234;
        var className1 = "Def";
        expectReadU2(providesIndex1);
        allowingLookupClass(providesIndex1, className1, "first provides lookup during construction");
        expectReadU2(0);

        // And
        var providesIndex2 = 345;
        var className2 = "Ghi";
        expectReadU2(providesIndex2);
        allowingLookupClass(providesIndex2, className2, "second provides lookup during construction");
        expectReadU2(0);

        // And
        var sut = new Module_attribute(mockConstantPool, mockOwner, mockIn);

        // When
        var actualProvides = sut.getProvides();

        //Then
        assertEquals("number of provides", providesCount, actualProvides.size());
        assertEquals(
                "first provides",
                providesIndex1,
                actualProvides.stream()
                        .mapToInt(ModuleProvides::getProvidesIndex)
                        .findFirst()
                        .orElseThrow());
        assertEquals(
                "second provides",
                providesIndex2,
                actualProvides.stream()
                        .skip(1)
                        .mapToInt(ModuleProvides::getProvidesIndex)
                        .findFirst()
                        .orElseThrow());
    }
}
