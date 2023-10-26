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

import com.jeantessier.classreader.Visitor;
import org.jmock.Expectations;

public class TestMethodParameters_attributeWithMethodParameters extends TestAnnotationsBase {
    public void testWithOneMethodParameter() throws Exception {
        // Given
        final int nameIndex = 123;
        final int accessFlags = 456;
        final String encodedName = "LAbc;";

        // And
        expectReadAttributeLength(5);
        expectReadNumParameters(1);
        expectReadU2(nameIndex);
        expectReadU2(accessFlags);
        expectLookupUtf8(nameIndex, encodedName);

        // When
        var sut = new MethodParameters_attribute(mockConstantPool, mockOwner, mockIn);

        // Then
        assertEquals("num method parameters", 1, sut.getMethodParameters().size());

        // And
        assertEquals("method parameter name", nameIndex, sut.getMethodParameters().stream().findFirst().orElseThrow().getNameIndex());
        assertEquals("method parameter access flags", accessFlags, sut.getMethodParameters().stream().findFirst().orElseThrow().getAccessFlag());
    }

    public void testWithMultipleMethodParameters() throws Exception {
        // Given
        final int nameIndex1 = 123;
        final int accessFlags1 = 456;
        final String encodedName1 = "LAbc;";

        // And
        final int nameIndex2 = 789;
        final int accessFlags2 = 987;
        final String encodedName2 = "LAbc;";

        // And
        expectReadAttributeLength(9);
        expectReadNumParameters(2);
        expectReadU2(nameIndex1);
        expectReadU2(accessFlags1);
        expectLookupUtf8(nameIndex1, encodedName1, "first method parameter");
        expectReadU2(nameIndex2);
        expectReadU2(accessFlags2);
        expectLookupUtf8(nameIndex2, encodedName2, "second method parameter");

        // When
        var sut = new MethodParameters_attribute(mockConstantPool, mockOwner, mockIn);

        // Then
        assertEquals("num method parameters", 2, sut.getMethodParameters().size());

        // And
        assertEquals("method parameter name", nameIndex1, sut.getMethodParameters().stream().findFirst().orElseThrow().getNameIndex());
        assertEquals("method parameter access flags", accessFlags1, sut.getMethodParameters().stream().findFirst().orElseThrow().getAccessFlag());

        // And
        assertEquals("method parameter name", nameIndex2, sut.getMethodParameters().stream().skip(1).findFirst().orElseThrow().getNameIndex());
        assertEquals("method parameter access flags", accessFlags2, sut.getMethodParameters().stream().skip(1).findFirst().orElseThrow().getAccessFlag());
    }
}
