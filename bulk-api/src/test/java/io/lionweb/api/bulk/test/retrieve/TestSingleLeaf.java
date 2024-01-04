package io.lionweb.api.bulk.test.retrieve;

import io.lionweb.lioncore.java.serialization.data.SerializedClassifierInstance;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestSingleLeaf extends ATestRetrieve {
  @Test
  public void depthinfinite() {
    List<SerializedClassifierInstance> roots = getBulk().retrieve(Arrays.asList("leaf-id"), null).getClassifierInstances();
    assertEquals(1, roots.size());
  }
  @Test
  public void depth0() {
    List<SerializedClassifierInstance> roots = getBulk().retrieve(Arrays.asList("leaf-id"), 0).getClassifierInstances();
    assertEquals(1, roots.size());
  }
  @Test
  public void depth1() {
    List<SerializedClassifierInstance> roots = getBulk().retrieve(Arrays.asList("leaf-id"), 1).getClassifierInstances();
    assertEquals(1, roots.size());
  }
  @Test
  public void depth2() {
    List<SerializedClassifierInstance> roots = getBulk().retrieve(Arrays.asList("leaf-id"), 2).getClassifierInstances();
    assertEquals(1, roots.size());
  }
}
