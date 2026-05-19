/*
 * JBoss, Home of Professional Open Source
 * Copyright 2026 Red Hat Inc. and/or its affiliates and other contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.integration.test.lifecycle;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.jboss.arquillian.integration.test.lifecycle.FileWriterExtension.TMP_FILE_ASSET_NAME;
import static org.jboss.arquillian.integration.test.lifecycle.FileWriterExtension.appendToFile;
import static org.jboss.arquillian.integration.test.lifecycle.FileWriterExtension.checkRunsWhere;
import static org.jboss.arquillian.integration.test.lifecycle.FileWriterExtension.getTmpFilePath;
import static org.jboss.arquillian.integration.test.lifecycle.FileWriterExtension.RunsWhere.SERVER;

@ExtendWith(FileWriterExtension.class)
@ArquillianTest
@ExpectedTrace("parameterized_test,parameterized_test,parameterized_test")
class ParameterizedTestTest {

    @Deployment
    static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(FileWriterExtension.class, ExpectedTrace.class)
                .addAsResource(new StringAsset(getTmpFilePath().toString()), TMP_FILE_ASSET_NAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    void parameterizedTest(String value) {
        appendToFile("parameterized_test");
        checkRunsWhere(SERVER);
    }
}
