/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.hadoop.store;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.hadoop.store.input.TextFileReader;
import org.springframework.data.hadoop.store.output.TextFileWriter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for reading and writing text using context configuration.
 *
 * @author Janne Valkealahti
 *
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TextFileStoreCtxTests extends AbstractStoreTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void testWriteReadManyLines() throws IOException, InterruptedException {

		TextFileWriter writer = context.getBean("writer", TextFileWriter.class);
		assertNotNull(writer);

		TestUtils.writeData(writer, new String[] { DATA10 }, false);
		Thread.sleep(2000);
		TestUtils.writeData(writer, new String[] { DATA11 }, false);
		Thread.sleep(2000);
		TestUtils.writeData(writer, new String[] { DATA12 }, true);

		TextFileReader reader1 = new TextFileReader(testConfig, testDefaultPath.suffix("0"), null);
		List<String> splitData1 = TestUtils.readData(reader1);

		TextFileReader reader2 = new TextFileReader(testConfig, testDefaultPath.suffix("1"), null);
		List<String> splitData2 = TestUtils.readData(reader2);

		TextFileReader reader3 = new TextFileReader(testConfig, testDefaultPath.suffix("2"), null);
		List<String> splitData3 = TestUtils.readData(reader3);

		assertThat(splitData1.size() + splitData2.size() + splitData3.size(), is(3));
	}

}
