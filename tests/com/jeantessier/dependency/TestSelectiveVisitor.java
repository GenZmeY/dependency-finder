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

package com.jeantessier.dependency;

import org.jmock.Expectations;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class TestSelectiveVisitor {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Visitor delegate;
    private TraversalStrategy strategy;

    private PackageNode packageNode;
    private ClassNode classNode;
    private FeatureNode featureNode;

    private SelectiveVisitor sut;

    @Before
    public void setUp() {
        context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);

        delegate = context.mock(Visitor.class);
        strategy = context.mock(TraversalStrategy.class);

        packageNode = context.mock(PackageNode.class);
        classNode = context.mock(ClassNode.class);
        featureNode = context.mock(FeatureNode.class);

        sut = new SelectiveVisitor(strategy);
        sut.setDelegate(delegate);
    }

    @Test
    public void testTraverseNodes_SortsTheNodesUsingTheStrategy() {
        final Collection<? extends Node> nodes = new ArrayList<>();
        final Collection<Node> sortedNodes = new ArrayList<>();
        sortedNodes.add(packageNode);

        context.checking(new Expectations() {{
           oneOf (strategy).order(nodes);
                will(returnValue(sortedNodes));
           oneOf (delegate).traverseNodes(sortedNodes);
        }});

        sut.traverseNodes(nodes);
    }

    @Test
    public void testTraverseInbound_SortsTheNodesUsingTheStrategy() {
        final Collection<? extends Node> nodes = new ArrayList<>();
        final Collection<Node> sortedNodes = new ArrayList<>();
        sortedNodes.add(packageNode);

        context.checking(new Expectations() {{
           oneOf (strategy).order(nodes);
                will(returnValue(sortedNodes));
           oneOf (delegate).traverseInbound(sortedNodes);
        }});

        sut.traverseInbound(nodes);
    }

    @Test
    public void testTraverseOutbound_SortsTheNodesUsingTheStrategy() {
        final Collection<? extends Node> nodes = new ArrayList<>();
        final Collection<Node> sortedNodes = new ArrayList<>();
        sortedNodes.add(packageNode);

        context.checking(new Expectations() {{
           oneOf (strategy).order(nodes);
                will(returnValue(sortedNodes));
           oneOf (delegate).traverseOutbound(sortedNodes);
        }});

        sut.traverseOutbound(nodes);
    }

    @Test
    public void testVisitPackageNode_NotInScope() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInScope(packageNode);
                will(returnValue(false));
        }});

        sut.visitPackageNode(packageNode);
    }

    @Test
    public void testVisitPackageNode_InScope() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInScope(packageNode);
                will(returnValue(true));
           oneOf (packageNode).accept(delegate);
        }});

        sut.visitPackageNode(packageNode);
    }

    @Test
    public void testVisitInboundPackageNode_NotInFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(packageNode);
                will(returnValue(false));
        }});

        sut.visitInboundPackageNode(packageNode);
    }

    @Test
    public void testVisitInboundPackageNode_InFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(packageNode);
                will(returnValue(true));
           oneOf (packageNode).acceptInbound(delegate);
        }});

        sut.visitInboundPackageNode(packageNode);
    }

    @Test
    public void testVisitOutboundPackageNode_NotInFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(packageNode);
                will(returnValue(false));
        }});

        sut.visitOutboundPackageNode(packageNode);
    }

    @Test
    public void testVisitOutboundPackageNode_InFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(packageNode);
                will(returnValue(true));
           oneOf (packageNode).acceptOutbound(delegate);
        }});

        sut.visitOutboundPackageNode(packageNode);
    }

    @Test
    public void testVisitClassNode_NotInScope() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInScope(classNode);
                will(returnValue(false));
        }});

        sut.visitClassNode(classNode);
    }

    @Test
    public void testVisitClassNode_InScope() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInScope(classNode);
                will(returnValue(true));
           oneOf (classNode).accept(delegate);
        }});

        sut.visitClassNode(classNode);
    }

    @Test
    public void testVisitInboundClassNode_NotInFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(classNode);
                will(returnValue(false));
        }});

        sut.visitInboundClassNode(classNode);
    }

    @Test
    public void testVisitInboundClassNode_InFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(classNode);
                will(returnValue(true));
           oneOf (classNode).acceptInbound(delegate);
        }});

        sut.visitInboundClassNode(classNode);
    }

    @Test
    public void testVisitOutboundClassNode_NotInFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(classNode);
                will(returnValue(false));
        }});

        sut.visitOutboundClassNode(classNode);
    }

    @Test
    public void testVisitOutboundClassNode_InFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(classNode);
                will(returnValue(true));
           oneOf (classNode).acceptOutbound(delegate);
        }});

        sut.visitOutboundClassNode(classNode);
    }

    @Test
    public void testVisitFeatureNode_NotInScope() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInScope(featureNode);
                will(returnValue(false));
        }});

        sut.visitFeatureNode(featureNode);
    }

    @Test
    public void testVisitFeatureNode_InScope() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInScope(featureNode);
                will(returnValue(true));
           oneOf (featureNode).accept(delegate);
        }});

        sut.visitFeatureNode(featureNode);
    }

    @Test
    public void testVisitInboundFeatureNode_NotInFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(featureNode);
                will(returnValue(false));
        }});

        sut.visitInboundFeatureNode(featureNode);
    }

    @Test
    public void testVisitInboundFeatureNode_InFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(featureNode);
                will(returnValue(true));
           oneOf (featureNode).acceptInbound(delegate);
        }});

        sut.visitInboundFeatureNode(featureNode);
    }

    @Test
    public void testVisitOutboundFeatureNode_NotInFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(featureNode);
                will(returnValue(false));
        }});

        sut.visitOutboundFeatureNode(featureNode);
    }

    @Test
    public void testVisitOutboundFeatureNode_InFilter() {
        context.checking(new Expectations() {{
           oneOf (strategy).isInFilter(featureNode);
                will(returnValue(true));
           oneOf (featureNode).acceptOutbound(delegate);
        }});

        sut.visitOutboundFeatureNode(featureNode);
    }
}
