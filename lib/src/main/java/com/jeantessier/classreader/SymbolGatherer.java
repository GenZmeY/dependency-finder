/*
 *  Copyright (c) 2001-2024, Jean Tessier
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

package com.jeantessier.classreader;

import java.util.*;
import java.util.stream.*;

public class SymbolGatherer extends VisitorBase {
    private final SymbolGathererStrategy strategy;

    private final Collection<String> collection = new ArrayList<>();

    private InnerClass innerClass;

    public SymbolGatherer(SymbolGathererStrategy strategy) {
        this.strategy = strategy;
    }

    public Stream<String> stream() {
        return collection.stream();
    }

    private void add(String element) {
        collection.add(element);
    }

    // Classfile
    public void visitClassfile(Classfile classfile) {
        innerClass = null;
        super.visitClassfile(classfile);

        if (innerClass != null) {
            if (strategy.isMatching(innerClass)) {
                add(innerClass.getInnerClassInfo());
            }
        } else if (strategy.isMatching(classfile)) {
            add(classfile.getClassName());
        }
    }

    // Features
    public void visitField_info(Field_info entry) {
        if (strategy.isMatching(entry)) {
            add(entry.getFullSignature());
        }

        super.visitField_info(entry);
    }

    public void visitMethod_info(Method_info entry) {
        if (strategy.isMatching(entry)) {
            add(entry.getFullSignature());
        }

        super.visitMethod_info(entry);
    }

    public void visitLocalVariable(LocalVariable helper) {
        if (strategy.isMatching(helper)) {
            add(strategy.locateMethodFor(helper) + ": " + helper.getName());
        }

        super.visitLocalVariable(helper);
    }

    public void visitInnerClass(InnerClass helper) {
        if (strategy.locateClassfileFor(helper).getRawClass() == helper.getRawInnerClassInfo()) {
            this.innerClass = helper;
        }

        super.visitInnerClass(helper);
    }
}
