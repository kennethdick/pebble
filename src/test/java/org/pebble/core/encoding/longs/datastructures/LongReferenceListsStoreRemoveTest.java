package org.pebble.core.encoding.longs.datastructures;

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

import it.unimi.dsi.fastutil.longs.LongList;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.pebble.UnitTest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Category(UnitTest.class)
public class LongReferenceListsStoreRemoveTest {

    @Test
    public void itShouldRemoveListSuccessfully() {
        final LongReferenceListsIndex referenceListsIndex = mock(LongReferenceListsIndex.class);
        final int size = 3;
        final int maxRecursiveReferences = 1;
        final int minListSize = 3;
        final LongReferenceListsStore referenceListsStore = new LongReferenceListsStore(
            size,
            maxRecursiveReferences,
            minListSize,
            referenceListsIndex
        );
        LongReferenceListsStore.ReferenceList referenceList = mock(LongReferenceListsStore.ReferenceList.class);

        referenceListsStore.remove(referenceList);

        verify(referenceListsIndex).removeListFromListsInvertedIndex(anyInt(), any(LongList.class));
    }

}
