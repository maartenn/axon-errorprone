package com.github.axon_errorprone;

import static com.google.errorprone.BugPattern.SeverityLevel.ERROR;
import static com.google.errorprone.matchers.Matchers.anyOf;
import static com.google.errorprone.matchers.Matchers.isSameType;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.LambdaExpressionTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MemberReferenceTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.google.errorprone.matchers.Matchers;
import com.google.errorprone.matchers.method.MethodMatchers;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import org.axonframework.queryhandling.SubscriptionQueryResult;

/**
 * Checker is hugely based on ByteBufferBackingArray (google error-prone) which does a similar though more
 * extensive check.
 */
@BugPattern(
    name = "InitialResultNotCalledOnSubscriptionQuery",
    summary =
        "Method initialResult() is not called but updates() is called",
    explanation =
        "Whenever a subscription query result is used and the updates() method is called then the "
            + "initialResult method has to be called first. This has to be done to make sure you "
            + "won't miss any updates, even if you don't expect any results at first. The "
            + "initialResult will make sure the updates will be buffered before it's called.",
    linkType = BugPattern.LinkType.CUSTOM,
    link = "https://github.com/maartenn/axon-errorprone/wiki/InitialResultNotCalledOnSubscriptionQuery",
    severity = ERROR)
@AutoService(BugChecker.class)
public class InitialResultNotCalledOnSubscriptionQuery extends BugChecker implements
    MethodInvocationTreeMatcher, MemberReferenceTreeMatcher {

    private static final Matcher<ExpressionTree> SQ_UPDATES_MATCHER =
        anyOf(
            Matchers.anyMethod().onDescendantOf(SubscriptionQueryResult.class.getName())
                .named("updates"));

    private static final Matcher<ExpressionTree> SQ_INITIALRESULT_MATCHER =
        anyOf(
            Matchers.anyMethod().onDescendantOf(SubscriptionQueryResult.class.getName())
                .named("initialResult"));

    private static final Matcher<ExpressionTree> SQ_MATCHER = isSameType(
        SubscriptionQueryResult.class);

    private static boolean isNotASubscriptionQueryResult(
        ExpressionTree receiver, VisitorState state) {
        return !SQ_MATCHER.matches(receiver, state);
    }


    @Override
    public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state) {
        if (!SQ_UPDATES_MATCHER.matches(tree, state)) {
            return Description.NO_MATCH;
        }

        return getDescription(tree, state);
    }

    @Override
    public Description matchMemberReference(MemberReferenceTree tree, VisitorState state) {
        if (!SQ_UPDATES_MATCHER.matches(tree, state)) {
            return Description.NO_MATCH;
        }
        return getDescription(tree, state);
    }

    private Description getDescription(ExpressionTree tree, VisitorState state) {
        // Checks for validating use on method call chain.
        ExpressionTree receiver = tree;
        do {
            receiver = ASTHelpers.getReceiver(receiver);
            if (isNotASubscriptionQueryResult(receiver, state)) {
                return Description.NO_MATCH;
            }
        } while (receiver instanceof MethodInvocationTree);

        Symbol bufferSymbol = ASTHelpers.getSymbol(receiver);
        if (bufferSymbol == null) {
            return Description.NO_MATCH;
        }

        // Checks for validating use on method scope.
        if (bufferSymbol.owner instanceof MethodSymbol) {
            MethodTree enclosingMethod = ASTHelpers.findMethod((MethodSymbol) bufferSymbol.owner,
                state);
            if (enclosingMethod == null || ValidSubscriptionQueryScanner.scan(enclosingMethod,
                state,
                bufferSymbol)
            ) {
                return Description.NO_MATCH;
            }
        }

        // Checks for validating use on fields.
        if (bufferSymbol.owner instanceof ClassSymbol) {
            return Description.NO_MATCH;
        }

        return describeMatch(tree);
    }


    /**
         * Scan for a call to SubscriptionQueryResult.initialResult() or ::initialResult
         */
    private static class ValidSubscriptionQueryScanner extends TreeScanner<Void, VisitorState> {

        private final Symbol searchedBufferSymbol;
        private boolean valid;


        private ValidSubscriptionQueryScanner(Symbol searchedBufferSymbol) {
            this.searchedBufferSymbol = searchedBufferSymbol;
        }

        static boolean scan(Tree tree, VisitorState state, Symbol searchedBufferSymbol) {
            ValidSubscriptionQueryScanner visitor = new ValidSubscriptionQueryScanner(
                searchedBufferSymbol);
            tree.accept(visitor, state);
            return visitor.valid;
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree tree, VisitorState state) {
            return matches(tree, state) ? null : super.visitMethodInvocation(tree, state);
        }

        @Override
        public Void visitMemberReference(MemberReferenceTree tree, VisitorState state) {
            return matches(tree, state) ? null :super.visitMemberReference(tree,state);
        }


        private boolean matches(ExpressionTree tree, VisitorState state) {
            if (valid) {
                return true;
            }
            Symbol bufferSymbol = ASTHelpers.getSymbol(ASTHelpers.getReceiver(tree));

            if (searchedBufferSymbol.equals(bufferSymbol) && SQ_INITIALRESULT_MATCHER.matches(tree,
                state)) {
                valid = true;
            }
            return false;
        }
    }

}
