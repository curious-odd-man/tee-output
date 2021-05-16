package com.github.curiousoddman.plugins.idea.tee.output;

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.execution.testframework.sm.runner.events.TestOutputEvent;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GradleSaver implements SMTRunnerEventsListener {
    private final TeeProcessOutputSaver mainSaver;

    public GradleSaver(TeeProcessOutputSaver mainSaver) {
        this.mainSaver = mainSaver;
    }

    @Override
    public void onTestingStarted(SMTestProxy.@NotNull SMRootTestProxy testsRoot) {
        mainSaver.onTextAvailable("Started testing: " + testsRoot.getPresentableName());
    }

    @Override
    public void onTestingFinished(SMTestProxy.@NotNull SMRootTestProxy testsRoot) {
        mainSaver.onTextAvailable("Finished testing: " + testsRoot.getPresentableName());
    }

    @Override
    public void onTestsCountInSuite(int count) {

    }

    @Override
    public void onTestStarted(@NotNull SMTestProxy test) {
        mainSaver.onTextAvailable("Started test: " + test.getPresentableName());
    }

    @Override
    public void onTestFinished(@NotNull SMTestProxy test) {
        mainSaver.onTextAvailable("Started test: " + test.getPresentableName());
    }

    @Override
    public void onTestFailed(@NotNull SMTestProxy test) {
        mainSaver.onTextAvailable("FAILED test: " + test.getPresentableName());
    }

    @Override
    public void onTestIgnored(@NotNull SMTestProxy test) {
        mainSaver.onTextAvailable("Ignored test: " + test.getPresentableName());
    }

    @Override
    public void onSuiteFinished(@NotNull SMTestProxy suite) {
        mainSaver.onTextAvailable("Finished suite: " + suite.getPresentableName());
    }

    @Override
    public void onSuiteStarted(@NotNull SMTestProxy suite) {
        mainSaver.onTextAvailable("Started suite: " + suite.getPresentableName());
    }

    @Override
    public void onCustomProgressTestsCategory(@Nullable String categoryName, int testCount) {

    }

    @Override
    public void onCustomProgressTestStarted() {

    }

    @Override
    public void onCustomProgressTestFailed() {

    }

    @Override
    public void onCustomProgressTestFinished() {

    }

    @Override
    public void onSuiteTreeNodeAdded(SMTestProxy testProxy) {

    }

    @Override
    public void onSuiteTreeStarted(SMTestProxy suite) {

    }

    @Override
    public void onTestOutput(@NotNull SMTestProxy proxy, @NotNull TestOutputEvent event) {
        mainSaver.onTextAvailable(event.getText(), event.getOutputType());
    }

    @Override
    public void onUncapturedOutput(@NotNull SMTestProxy activeProxy, String text, Key type) {
        mainSaver.onTextAvailable(text, type);
    }
}
