package org.pebble.core.encoding;

/**
 *  Copyright 2015 Groupon
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.pebble.UnitTest;
import org.pebble.core.exceptions.NotStrictlyIncrementalListException;

import static junit.framework.TestCase.assertEquals;
import static org.pebble.core.encoding.Helper.getOutput;
import static org.pebble.core.encoding.Helper.toBinaryString;

@Category(UnitTest.class)
public class IntsOutputSuccinctStreamWriteIntervalsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void whenThereIsNotIntervalsOnListItShouldWriteIntervalsSuccinctRepresentationSuccessfully()
        throws Exception
    {
        final int valueBitSize = 1;
        final IntList list = new IntArrayList(new int[] {1, 2, 3, 5, 6, 7, 10, 11, 16, 19});
        final String expectedOutput = "1".replace(" ", "");
        final int expectedOffset = 1;
        final Helper.Output out = getOutput();

        final int offset = out.stream.writeIntervals(list, valueBitSize);
        out.close();

        assertEquals(expectedOutput, toBinaryString(out.buffer, offset));
        assertEquals(expectedOffset, offset);
    }

    @Test
    public void whenListIsSmallerThanMinIntervalSizeItShouldWriteIntervalsSuccinctRepresentationSuccessfully()
        throws Exception
    {
        final int valueBitSize = 1;
        final IntList list = new IntArrayList(new int[] {1, 3, 4});
        final String expectedOutput = "1".replace(" ", "");
        final int expectedOffset = 1;
        final Helper.Output out = getOutput();

        final int offset = out.stream.writeIntervals(list, valueBitSize);
        out.close();

        assertEquals(expectedOutput, toBinaryString(out.buffer, offset));
        assertEquals(expectedOffset, offset);
    }

    @Test
    public void whenThereIsIntervalsOnListItShouldWriteIntervalsSuccinctRepresentationSuccessfully() throws Exception {
        final int valueBitSize = 3;
        final IntList list = new IntArrayList(
            new int[] {1, 3, 4, 5, 7, 8, 9, 10, 12, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 30}
        );
        /**
         * 3 [7, 8, 9, 10] [14, 15, 16, 17] [19, 20, 21, 22, 23, 24] Intervals from list.
         * 3 [7, 10]       [14, 17]         [19, 24]                 Interval format.
         * 3 [7, 0]        [2,  0]          [0, 2]                   Interval delta format.
         * 4     7   1 3    1 1 3                                    Add 1 to ensure non zeros.
         * 001   111 1 11   1 1 11                                   Binary representation.
         * 3-00  111 1 2-1  1 1 2-1                                  Decimal Gamma Prefix and Binary Gamma Suffix.
         * 11-00 111 1 01-1 1 1 01-1                                 Binary Gamma Prefix and Binary Gamma Suffix.
         * 01100 111 1 0101 1 1 0101                                 Delta Encoding.
         */
        final String expectedOutput = "01100 111 1 0101 1 1 0101".replace(" ", "");
        final int expectedOffset = 19;
        final Helper.Output out = getOutput();

        final int offset = out.stream.writeIntervals(list, valueBitSize);
        out.close();

        assertEquals(expectedOutput, toBinaryString(out.buffer, offset));
        assertEquals(expectedOffset, offset);
    }

    @Test
    public void whenThereIsIntervalsAtEndOnListItShouldWriteIntervalsSuccinctRepresentationSuccessfully()
        throws Exception
    {
        final int valueBitSize = 3;
        final IntList list = new IntArrayList(
            new int[] {1, 3, 4, 5, 7, 8, 9, 10, 12, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24}
        );
        /**
         * 3 [7, 8, 9, 10] [14, 15, 16, 17] [19, 20, 21, 22, 23, 24] Intervals from list.
         * 3 [7, 10]       [14, 17]         [19, 24]                 Interval format.
         * 3 [7, 0]        [2,  0]          [0, 2]                   Interval delta format.
         * 4     7   1 3    1 1 3                                    Add 1 to ensure non zeros.
         * 001   111 1 11   1 1 11                                   Binary representation.
         * 3-00  111 1 2-1  1 1 2-1                                  Decimal Gamma Prefix and Binary Gamma Suffix.
         * 11-00 111 1 01-1 1 1 01-1                                 Binary Gamma Prefix and Binary Gamma Suffix.
         * 01100 111 1 0101 1 1 0101                                 Delta Encoding.
         */
        final String expectedOutput = "01100 111 1 0101 1 1 0101".replace(" ", "");
        final int expectedOffset = 19;
        final Helper.Output out = getOutput();

        final int offset = out.stream.writeIntervals(list, valueBitSize);
        out.close();

        assertEquals(expectedOutput, toBinaryString(out.buffer, offset));
        assertEquals(expectedOffset, offset);
    }

    @Test
    public void whenListIsOneSingleIntervalItShouldWriteIntervalsSuccinctRepresentationSuccessfully() throws Exception {
        final int valueBitSize = 3;
        final IntList list = new IntArrayList(new int[] {7, 8, 9, 10});
        /**
         * 1 [7, 8, 9, 10] Intervals from list.
         * 1 [7, 10]       Interval format.
         * 1 [7, 0]        Interval delta format.
         * 2    7   1      Add 1 to ensure non zeros.
         * 01   111 1      Binary representation.
         * 2-0  111 1      Decimal Gamma Prefix and Binary Gamma Suffix.
         * 01-0 111 1      Binary Gamma Prefix and Binary Gamma Suffix.
         * 0100 111 1      Delta Encoding.
         */
        final String expectedOutput = "0100 111 1".replace(" ", "");
        final int expectedOffset = 8;
        final Helper.Output out = getOutput();

        final int offset = out.stream.writeIntervals(list, valueBitSize);
        out.close();

        assertEquals(expectedOutput, toBinaryString(out.buffer, offset));
        assertEquals(expectedOffset, offset);
    }

    @Test
    public void itShouldThrowAnExceptionWhenListIsNonStrictlyIncremental() throws Exception {
        final int valueBitSize = 1;
        final IntList list = new IntArrayList(new int[] {1, 2, 3, 5, 0, 7, 10, 11, 16, 19});
        final Helper.Output out = getOutput();
        expectedException.expect(NotStrictlyIncrementalListException.class);
        expectedException.expectMessage("List is not strictly incremental, found 0 after 5");

        out.stream.writeIntervals(list, valueBitSize);
    }

}
