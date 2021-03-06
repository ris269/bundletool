/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.tools.build.bundletool.splitters;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.android.tools.build.bundletool.model.ModuleSplit;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import java.util.Collection;

/** Pipeline chaining the execution of module splitters. */
@AutoValue
public abstract class SplittingPipeline {

  public abstract ImmutableList<ModuleSplitSplitter> getSplitters();

  public ImmutableCollection<ModuleSplit> split(ModuleSplit split) {
    ImmutableList<ModuleSplit> splits = ImmutableList.of(split);
    for (ModuleSplitSplitter splitter : getSplitters()) {
      splits =
          splits
              .stream()
              .map(splitter::split)
              .flatMap(Collection::stream)
              .collect(toImmutableList());
    }
    return splits;
  }

  public static SplittingPipeline create(ImmutableList<ModuleSplitSplitter> splitters) {
    return new AutoValue_SplittingPipeline(splitters);
  }
}
