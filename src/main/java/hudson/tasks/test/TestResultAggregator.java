/*
 * The MIT License
 * 
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi, Yahoo!, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.tasks.test;

import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.BuildListener;

import java.io.IOException;

/**
 * Aggregates {@link AbstractTestResultAction}s of {@link MatrixRun}s
 * into {@link MatrixBuild}.
 * 
 * @author Kohsuke Kawaguchi
 */
public class TestResultAggregator extends MatrixAggregator {
    private MatrixTestResult result;

    public TestResultAggregator(MatrixBuild build, Launcher launcher, BuildListener listener) {
        super(build, launcher, listener);
    }

    @Override
    public boolean startBuild() throws InterruptedException, IOException {
        result = new MatrixTestResult();
        build.addAction(result);
        return true;
    }

    @Override
    public boolean endRun(MatrixRun run) throws InterruptedException, IOException {
        final AbstractTestResultAction atr = run.getAction(AbstractTestResultAction.class);
        if (result == null) {
            throw new IOException("The buid has not been started yet.");
        }
        if (atr != null) {
            result.add(atr);
        }
        return true;
    }
}
