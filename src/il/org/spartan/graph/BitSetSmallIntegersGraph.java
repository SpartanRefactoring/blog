/**
 *
 */
package il.org.spartan.graph;

import static il.org.spartan.azzert.*;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.*;

/** An immutable undirected graph whose nodes are short integers in the range
 * 0..N where N is small, with the compact representation of an array of size N
 * of arrays of shorts.
 * @author Yossi Gil
 * @since Apr 18, 2012 */
public class BitSetSmallIntegersGraph extends SmallIntegersGraph {
  private final BitSet[] neighbors;

  private BitSetSmallIntegersGraph(final BitSet[] neighbors, final int arcsCount, final short[] component, final short[] nodes) {
    super(arcsCount, nodes, component);
    this.neighbors = neighbors;
  }

  @Override public boolean has(final int n) {
    return n >= 0 && n < neighbors.length && neighbors[n] != null;
  }

  @Override public boolean has(final int n1, final int n2) {
    return has(n1) && has(n2) && neighbors[n1].get(n2);
  }

  public BitSet neighbors(final int n) {
    return n < 0 || n >= neighbors.length ? null : (BitSet) neighbors[n].clone();
  }

  public short[] nodes() {
    return nodes.clone();
  }

  public static class Builder {
    private static short[] append(final short[] as, final short a) {
      if (Arrays.binarySearch(as, a) >= 0)
        return as;
      final short[] $ = copyOf(as, as.length + 1);
      $[$.length - 1] = a;
      sort($);
      return $;
    }

    private static short makeShort(final int n) {
      if (n < 0 || n > Short.MAX_VALUE)
        throw new IllegalArgumentException();
      return (short) n;
    }

    private BitSet[] neighbors = new BitSet[0];
    private short[] component = new short[0];
    private short[] nodes = new short[0];

    public Builder add(final int n) {
      return add(makeShort(n));
    }

    public Builder add(final short n) {
      final int m = neighbors.length;
      if (n >= m) {
        neighbors = copyOf(neighbors, n + 1);
        component = copyOf(component, n + 1);
        Arrays.fill(component, m, component.length, (short) -1);
      }
      nodes = append(nodes, n);
      neighbors[n] = defaults.to(neighbors[n], new BitSet());
      return this;
    }

    public Builder connect(final int i, final int j) {
      return connect(makeShort(i), makeShort(j));
    }

    public Builder connect(final short i, final short j) {
      add(i).add(j);
      neighbors[i].set(j);
      neighbors[j].set(i);
      return union(i, j);
    }

    @SuppressWarnings("synthetic-access") //
    public BitSetSmallIntegersGraph go() {
      return new BitSetSmallIntegersGraph(neighbors, countArcs(), component, nodes);
    }

    private int countArcs() {
      int $ = 0;
      for (final BitSet ss : neighbors)
        if (ss != null)
          $ += ss.cardinality();
      return $ / 2;
    }

    private short find(final short n) {
      return component[n] < 0 ? n : (component[n] = find(component[n]));
    }

    private Builder union(final short n1, final short n2) {
      if (find(n1) != find(n2))
        component[find(n1)] = find(n2);
      return this;
    }
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    private static final int PSEUDO_ZERO = 2 * Short.MAX_VALUE + 2;

    @Test public void arsInsertedInNoOrder() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      azzert.that(g.arcsCount, is(3));
    }

    @Test public void connectedCheckDisconnectedNodes() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).add(1).go();
      assert !g.connected(0, 1);
    }

    @Test public void connectedCheckRemoteNodes() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).connect(1, 2).go();
      assert g.connected(0, 2);
    }

    @Test public void connectedExists() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assert !g.connected(2, 3);
    }

    @Test public void connectedOfPresentNodes() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assert g.connected(0, 1);
    }

    @Test public void connectedTwoChains() {
      final BitSetSmallIntegersGraph g = new Builder()//
          .connect(0, 1).connect(1, 2).connect(2, 3)//
          .connect(4, 5).connect(5, 6).connect(6, 7)//
          .go();
      assert g.connected(0, 2);
      assert g.connected(0, 1);
      assert g.connected(2, 1);
      assert g.connected(0, 3);
      assert g.connected(4, 7);
      assert g.connected(5, 7);
      assert g.connected(6, 7);
      assert g.connected(4, 7);
    }

    @Test public void containsArcFalse() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assert !g.has(13, 0);
    }

    @Test public void containsArcFirstLargeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(Short.MAX_VALUE + 1, 0);
    }

    @Test public void containsArcFirstNegativeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(-1, 0);
    }

    @Test public void containsArcSecondLargeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(0, Short.MAX_VALUE + 1);
    }

    @Test public void containsArcSecondLargeValueAfterInsertion1() {
      final BitSetSmallIntegersGraph g = new Builder().connect(1, 0).go();
      assert !g.has(1, PSEUDO_ZERO);
    }

    @Test public void containsArcSecondLargeValueAfterInsertion2() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assert !g.has(PSEUDO_ZERO, 0);
    }

    @Test public void containsArcSecondNegativeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(0, -1);
    }

    @Test public void containsArcTrue() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assert g.has(13, 14);
    }

    @Test public void containsLargeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(Short.MAX_VALUE + 1);
    }

    @Test public void containsNegativeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(-1);
    }

    @Test public void containsPseudoZero() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).go();
      assert !g.has(PSEUDO_ZERO);
    }

    @Test public void disconnectedComponentsBuilder() {
      final Builder b = new Builder().add(5).add(6).add(7).add(8).add(9);
      azzert.that(b.component[5], is(-1));
      azzert.that(b.component[6], is(-1));
      azzert.that(b.component[7], is(-1));
      azzert.that(b.component[0], is(-1));
      azzert.that(b.component[8], is(-1));
    }

    @Test public void disconnectedComponentsBuilderFreeNodes() {
      final Builder b = new Builder().add(5).add(6).add(7).add(8).add(9);
      azzert.that(b.component[0], is(-1));
      azzert.that(b.component[8], is(-1));
    }

    @Test public void disconnectedComponentsCount() {
      final Builder b = new Builder();
      b.add(5);
      b.add(6);
      b.add(7);
      b.add(9);
      azzert.that(b.go().components(), is(4));
    }

    @Test public void disconnectedComponentsGraph() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component[5], is(-1));
      azzert.that(g.component[6], is(-1));
      azzert.that(g.component[7], is(-1));
      azzert.that(g.component[0], is(-1));
      azzert.that(g.component[8], is(-1));
    }

    @Test public void disconnectedComponentsGraphFreeNodes() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component[0], is(-1));
      azzert.that(g.component[8], is(-1));
    }

    @Test public void disconnectedComponentsGraphFreeNodesFunction() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component((short) 0), is(0));
      azzert.that(g.component((short) 8), is(8));
    }

    @Test public void disconnectedComponentsGraphFunction() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component((short) 5), is(5));
      azzert.that(g.component((short) 6), is(6));
      azzert.that(g.component((short) 7), is(7));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesPublicFunctionCall() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(9).add(100).add(9).go();
      azzert.that(g.component(8), is(-1));
      azzert.that(g.component(0), is(-1));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesShortPrivateFunctionCall() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component((short) 8), is(8));
      azzert.that(g.component((short) 0), is(0));
    }

    @Test public void edgeFind() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      azzert.that(b.find((short) 0), is(0));
      azzert.that(b.find((short) 1), is(1));
      b.connect(0, 1);
      azzert.that(b.find((short) 0), is(1));
      azzert.that(b.find((short) 1), is(1));
    }

    @Test public void emptyCreationArcsCount() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      azzert.that(g.arcsCount, is(0));
    }

    @Test public void emptyCreationNodesCount() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      azzert.that(g.nodesCount(), is(0));
    }

    @Test public void emptyDoesNotContain() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(13);
    }

    @Test public void emptyDoesNotContainNegative() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert !g.has(-1);
    }

    @Test public void fourEdgeFindIsTrimming() {
      final Builder b = new Builder();
      b.connect(0, 1);
      b.connect(1, 2);
      b.connect(2, 3);
      b.connect(3, 4);
      azzert.that(b.component[0], is(1));
      azzert.that(b.component[1], is(2));
      azzert.that(b.component[2], is(3));
      azzert.that(b.component[3], is(4));
      azzert.that(b.component[4], is(-1));
      b.find((short) 0);
      azzert.that(b.component[0], is(4));
      azzert.that(b.component[1], is(4));
      azzert.that(b.component[2], is(4));
      azzert.that(b.component[3], is(4));
      azzert.that(b.component[4], is(-1));
    }

    @Test public void hasThreeNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      azzert.that(g.neighbors(13).cardinality(), is(3));
    }

    @Test public void illegalNodeNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      azzert.isNull(g.neighbors(15));
    }

    @Test public void nastyReconnection() {
      final BitSetSmallIntegersGraph g = new Builder()//
          .connect(1, 2).connect(2, 3).connect(3, 4).connect(4, 5) //
          .connect(1, 5) // find(1) shall return 5 and will try to reconnect
          .go();
      azzert.that(g.component(5), is(5));
      azzert.that(g.component(4), is(5));
      azzert.that(g.component(3), is(5));
      azzert.that(g.component(2), is(5));
      azzert.that(g.component(1), is(5));
    }

    @Test(timeout = 20) public void neighborsIsNotNull() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assert null != g.neighbors(13);
    }

    @Test public void newArcReturnsThis() {
      final Builder b = new Builder();
      assertSame(b, b.connect(13, 14));
    }

    public void newNodeIdempotent() {
      azzert.that(new Builder().add(5).add(5).add(5).go().nodesCount(), is(1));
    }

    @Test(expected = IllegalArgumentException.class) public void newNodeLargeNumber() {
      new Builder().add(Short.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class) public void newNodeNegative() {
      new Builder().add(-1);
    }

    @Test public void newNodeReturnsThis() {
      final Builder b = new Builder();
      assertSame(b, b.add(13));
    }

    @Test public void noComponents() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      azzert.that(g.components(), is(0));
    }

    @Test public void nodesEmpty() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      azzert.that(g.nodes().length, is(0));
    }

    @Test public void nodesExists() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assert null != g.nodes();
    }

    @Test public void nodesResistChange() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      g.nodes()[0] = 5;
      azzert.that(g.nodes()[0], is(1));
      azzert.that(g.nodes()[1], is(2));
      azzert.that(g.nodes()[2], is(3));
      azzert.that(g.nodes()[3], is(4));
    }

    @Test public void nodesSingle() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      azzert.that(g.nodes().length, is(1));
    }

    @Test public void nodesSingleCorrect() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      azzert.that(g.nodes()[0], is(1));
    }

    @Test public void nodesTwoCorrect() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).add(2).go();
      azzert.that(g.nodes()[0], is(1));
      azzert.that(g.nodes()[1], is(2));
    }

    @Test public void nodesUnsortedCorrect() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      azzert.that(g.nodes()[0], is(1));
      azzert.that(g.nodes()[1], is(2));
      azzert.that(g.nodes()[2], is(3));
      azzert.that(g.nodes()[3], is(4));
    }

    @Test public void noNullCreation() {
      assert null != new Builder().go();
    }

    @Test public void oneComponentOfOneEdge() {
      final BitSetSmallIntegersGraph g = new Builder().connect(1, 2).go();
      azzert.that(g.components(), is(1));
    }

    @Test public void oneComponents() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      azzert.that(g.components(), is(1));
    }

    @Test public void safeModifictation() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      g.neighbors(0).clear(1);
      assert g.has(0, 1);
    }

    @Test public void selfEdgeComponent() {
      final Builder b = new Builder();
      b.connect(5, 5);
      azzert.that(b.component[5], is(-1));
    }

    @Test public void selfEdgeFind() {
      final Builder b = new Builder();
      b.connect(5, 5);
      azzert.that(b.find((short) 5), is(5));
    }

    @Test public void simpleFind() {
      final Builder b = new Builder();
      b.add(0);
      azzert.that(b.find((short) 0), is(0));
    }

    @Test public void simpleNode0ContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).go();
      assert g.has(0);
    }

    @Test public void simpleNode1ContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      assert g.has(1);
    }

    @Test public void simpleTwoNodeContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).add(1).go();
      assert g.has(0);
      assert g.has(1);
    }

    @Test public void singleArcCountArcs() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      azzert.that(g.arcsCount, is(1));
    }

    @Test public void singleArcCountNodes() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      azzert.that(g.nodesCount(), is(2));
    }

    @Test public void singleArcHasOneNeighbor() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      azzert.that(g.neighbors(13).cardinality(), is(1));
    }

    @Test public void singleArcInsertedTwiceCountArcs() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 14).go();
      azzert.that(g.arcsCount, is(1));
    }

    @Test public void singleArcInverseHasOneNeighbor() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      azzert.that(g.neighbors(14).cardinality(), is(1));
    }

    @Test public void singleEdgeCheckComponent() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      b.connect(0, 1);
      final BitSetSmallIntegersGraph g = b.go();
      azzert.that(g.component((short) 0), is(1));
      azzert.that(g.component((short) 1), is(1));
    }

    @Test public void singleNodeContainsFalse() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assert !g.has(14);
    }

    @Test public void singleNodeContainsFalseBelow() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assert !g.has(11);
    }

    @Test public void singleNodeContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assert g.has(13);
    }

    @Test public void singleNodeCountArcs() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      azzert.that(g.arcsCount, is(0));
    }

    @Test public void singleNodeCountNodes() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      azzert.that(g.nodesCount(), is(1));
    }

    @Test public void singleNodeHasNoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      azzert.that(g.neighbors(13).cardinality(), is(0));
    }

    @Test public void triangleAndPath() {
      final BitSetSmallIntegersGraph g = new Builder()//
          .connect(1, 2).connect(2, 3).connect(3, 4) //
          .connect(7, 8).connect(5, 6).connect(6, 7) //
          .go();
      azzert.that(g.components(), is(2));
    }

    @Test public void triangleHasTwoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      azzert.that(g.neighbors(14).cardinality(), is(2));
      azzert.that(g.neighbors(13).cardinality(), is(2));
      azzert.that(g.neighbors(5).cardinality(), is(2));
    }

    @Test public void twoArcsHasTwoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      azzert.that(g.neighbors(13).cardinality(), is(2));
    }

    @Test public void twoArcsInverseHasTwoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      azzert.that(g.neighbors(14).cardinality(), is(1));
      azzert.that(g.neighbors(15).cardinality(), is(1));
    }

    @Test public void twoEdgeComponent() {
      final Builder b = new Builder();
      b.connect(0, 1);
      b.connect(1, 2);
      azzert.that(b.component[0], is(1));
      azzert.that(b.component[1], is(2));
      azzert.that(b.component[1], is(2));
    }

    @Test public void twoEdgeFind() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      azzert.that(b.find((short) 0), is(0));
      azzert.that(b.find((short) 1), is(1));
      b.connect(0, 1);
      azzert.that(b.find((short) 0), is(1));
      azzert.that(b.find((short) 1), is(1));
      b.connect(1, 2);
      azzert.that(b.find((short) 0), is(2));
      azzert.that(b.find((short) 1), is(2));
      azzert.that(b.find((short) 2), is(2));
    }

    @Test public void twoNodeContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).add(14).go();
      assert g.has(13);
      assert g.has(14);
    }
  }
}