/*
 *  Copyright (c) 2001-2008, Jean Tessier
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

import java.io.*;

import org.jmock.*;
import org.jmock.integration.junit3.*;
import org.jmock.lib.legacy.*;

import com.jeantessier.classreader.*;

public class TestAttributeBase extends MockObjectTestCase {
    protected Classfile mockClassfile;
    protected Visitable mockOwner;
    protected DataInput mockIn;

    protected Sequence dataReads;

    protected void setUp() throws Exception {
        super.setUp();

        setImposteriser(ClassImposteriser.INSTANCE);

        mockClassfile = mock(Classfile.class);
        mockOwner = mock(Visitable.class);
        mockIn = mock(DataInput.class);

        dataReads = sequence("dataReads");
    }

    protected void expectReadAttributeLength(final int length) throws IOException {
        expectReadU4(length);
    }

    protected void expectReadNumAnnotations(int numAnnotations) throws IOException {
        expectReadU2(numAnnotations);
    }

    protected void expectReadNumParameters(int numParameters) throws IOException {
        expectReadU1(numParameters);
    }

    protected void expectReadAnnotation(int typeIndex, int numElementValuePairs) throws IOException {
        expectReadTypeIndex(typeIndex);
        expectReadNumElementValuePairs(numElementValuePairs);
    }

    protected void expectReadTypeIndex(int typeIndex) throws IOException {
        expectReadU2(typeIndex);
    }

    protected void expectReadNumElementValuePairs(int numElementValuePairs) throws IOException {
        expectReadU2(numElementValuePairs);
    }

    protected void expectReadU1(final int i) throws IOException {
        checking(new Expectations() {{
            one (mockIn).readUnsignedByte();
                inSequence(dataReads);
                will(returnValue(i));
        }});
    }

    protected void expectReadU2(final int i) throws IOException {
        checking(new Expectations() {{
            one (mockIn).readUnsignedShort();
                inSequence(dataReads);
                will(returnValue(i));
        }});
    }

    protected void expectReadU4(final int i) throws IOException {
        checking(new Expectations() {{
            one (mockIn).readInt();
                inSequence(dataReads);
                will(returnValue(i));
        }});
    }

    protected void expectReadUtf(final String s) throws IOException {
        checking(new Expectations() {{
            one (mockIn).readUTF();
                inSequence(dataReads);
                will(returnValue(s));
        }});
    }

    protected void expectLookupInteger(final int index, final int value) {
        final ConstantPool mockConstantPool = mock(ConstantPool.class);
        final Integer_info mockInteger_info = mock(Integer_info.class);

        checking(new Expectations() {{
            one (mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            one (mockConstantPool).get(index);
                will(returnValue(mockInteger_info));
            one (mockInteger_info).getValue();
                will(returnValue(value));
        }});
    }

    protected void expectLookupLong(final int index, final long value) {
        final ConstantPool mockConstantPool = mock(ConstantPool.class);
        final Long_info mockLong_info = mock(Long_info.class);

        checking(new Expectations() {{
            one (mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            one (mockConstantPool).get(index);
                will(returnValue(mockLong_info));
            one (mockLong_info).getValue();
                will(returnValue(value));
        }});
    }

    protected void expectLookupFloat(final int index, final float value) {
        final ConstantPool mockConstantPool = mock(ConstantPool.class);
        final Float_info mockFloat_info = mock(Float_info.class);

        checking(new Expectations() {{
            one (mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            one (mockConstantPool).get(index);
                will(returnValue(mockFloat_info));
            one (mockFloat_info).getValue();
                will(returnValue(value));
        }});
    }

    protected void expectLookupDouble(final int index, final double value) {
        final ConstantPool mockConstantPool = mock(ConstantPool.class);
        final Double_info mockDouble_info = mock(Double_info.class);

        checking(new Expectations() {{
            one (mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            one (mockConstantPool).get(index);
                will(returnValue(mockDouble_info));
            one (mockDouble_info).getValue();
                will(returnValue(value));
        }});
    }

    protected void expectLookupString(final int index, final String value) {
        final ConstantPool mockConstantPool = mock(ConstantPool.class);
        final String_info mockString_info = mock(String_info.class);

        checking(new Expectations() {{
            one (mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            one (mockConstantPool).get(index);
                will(returnValue(mockString_info));
            one (mockString_info).getValue();
                will(returnValue(value));
        }});
    }

    protected void expectLookupUtf8(final int index, final String value) {
        final ConstantPool mockConstantPool = mock(ConstantPool.class);
        final UTF8_info mockUtf8_info = mock(UTF8_info.class);

        checking(new Expectations() {{
            one (mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            one (mockConstantPool).get(index);
                will(returnValue(mockUtf8_info));
            one (mockUtf8_info).getValue();
                will(returnValue(value));
        }});
    }
}
