package org.thegalactic.lattice;

/*
 * ClosureSystem.java
 *
 * Copyright: 2010-2015 Karell Bertet, France
 * Copyright: 2015-2016 The Galactic Organization, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices.
 * You can redistribute it and/or modify it under the terms of the CeCILL-B license.
 */
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.thegalactic.dgraph.DAGraph;
import org.thegalactic.dgraph.ConcreteDGraph;
import org.thegalactic.dgraph.Node;
import org.thegalactic.util.ComparableSet;

/**
 * This class is an abstract class defining the common behavior of closure
 * systems, and specialy its closed set lattice generation.
 *
 * Both a context and an implicational system have properties of a closure
 * system, and therefore extend this class.
 *
 * A closure system is formaly defined by a set of indexed elements and a
 * closure operator (abstract methods {@link #getSet} and {@link #closure}).
 *
 * Abstract method {@link #save} also describe the common behavior of a closure
 * system.
 *
 * However, this abstract class provides both abstract and non abstract methods.
 *
 * Although abstract methods depends on data, and so have to be implemented by
 * each extended class, non abstract methods only used property of a closure
 * system. It is the case for methods {@link #nextClosure} (that computes the
 * next closure of the specified one according to the lectic order implemented
 * the well-known Wille algorithm) invoked by method {@link #allClosures} and
 * the main method {@link #closedSetLattice} (where lattice can be transitively
 * closed or reduced).
 *
 *
 * ![ClosureSystem](ClosureSystem.png)
 *
 * @uml ClosureSystem.png
 * !include resources/org/thegalactic/lattice/ClosureSystem.iuml
 *
 * hide members
 * show ClosureSystem members
 * class ClosureSystem #LightCyan
 * title ClosureSystem UML graph
 */
public abstract class ClosureSystem {

    /*
     * ------------- ABSTRACT METHODS ------------------
     */
    /**
     * Returns the set of elements of the closure system.
     *
     * @return the set of elements of the closure system
     */
    public abstract SortedSet<Comparable> getSet();

    /**
     * Returns the closure of the specified set.
     *
     * @param set The specified set
     *
     * @return The closure
     */
    public abstract TreeSet<Comparable> closure(TreeSet<Comparable> set);

    /**
     * Saves this component in a file which name is specified.
     *
     * @param file name of file
     *
     * @throws IOException When an IOException occurs
     */
    public abstract void save(String file) throws IOException;

    /*
     * ------------- IMPLEMENTED METHODS ------------------
     */
    /**
     * Returns the closed set lattice of this component.
     *
     * A true value of the boolean `diagram` indicates that the Hasse diagramm
     * of the lattice is computed (i.e. it is transitively reduced), whereas a
     * false value indicates that the lattice is transitively closed
     *
     * A transitively reduced lattice is generated by the static method
     * `ConceptLattice diagramLattice (ClosureSystem init)` that implements an
     * adaptation of Bordat's algorithm. This adaptation computes the dependance
     * graph while the lattice is generated, with the same complexity.
     *
     * A transitively closed lattice is generated bye well-known Next Closure
     * algorithm. In this case, the dependance graph of the lattice isn't
     * computed.
     *
     * @param diagram a boolean indicating if the Hasse diagramm of the lattice
     *                is computed or not.
     *
     * @return The concept lattice
     */
    public ConceptLattice closedSetLattice(boolean diagram) {
        if (diagram) {
            return ConceptLattice.diagramLattice(this);
        } else {
            return ConceptLattice.completeLattice(this);
        }
    }

    /**
     * Returns the lattice of this component.
     *
     * @return The concept lattice
     */
    public ConceptLattice lattice() {
        return this.closedSetLattice(true);
    }

    /**
     * Returns all the closed sets of the specified closure system (that can be
     * an IS or a context).
     *
     * Closed sets are generated in lecticaly order, with the emptyset's closure
     * as first closed set, using the Ganter's Next Closure algorithm.
     *
     * Therefore, closed sets have to be comparable using `ComparableSet` class.
     * This treatment is performed in O(cCl|S|^3) where S is the initial set of
     * elements, c is the number of closed sets that could be exponential in the
     * worst case, and Cl is the closure computation complexity.
     *
     * @return all the closeds set in the lectically order.
     */
    public Vector<Concept> allClosures() {
        Vector<Concept> allclosure = new Vector<Concept>();
        // first closure: closure of the empty set
        allclosure.add(new Concept(this.closure(new ComparableSet()), false));
        Concept cl = allclosure.firstElement();
        // next closures in lectically order
        boolean continu = true;
        do {
            cl = this.nextClosure(cl);
            if (allclosure.contains(cl)) {
                continu = false;
            } else {
                allclosure.add(cl);
            }
        } while (continu);

        return allclosure;
    }

    /**
     * Returns the lecticaly next closed set of the specified one.
     *
     * This treatment is an implementation of the best knowm algorithm of Wille
     * whose complexity is in O(Cl|S|^2), where S is the initial set of
     * elements, and Cl is the closure computation complexity.
     *
     * @param cl a concept
     *
     * @return the lecticaly next closed set
     */
    public Concept nextClosure(Concept cl) {
        TreeSet<Comparable> set = new TreeSet(this.getSet());
        boolean success = false;
        TreeSet setA = new TreeSet(cl.getSetA());
        Comparable ni = set.last();
        do {
            ni = (Comparable) set.last();
            set.remove(ni);
            if (!setA.contains(ni)) {
                setA.add(ni);
                TreeSet setB = this.closure(setA);
                setB.removeAll(setA);
                if (setB.isEmpty() || ((Comparable) setB.first()).compareTo(ni) >= 1) {
                    setA = this.closure(setA);
                    success = true;
                } else {
                    setA.remove(ni);
                }
            } else {
                setA.remove(ni);
            }
        } while (!success && ni.compareTo(this.getSet().first()) >= 1);
        return new Concept(setA, false);
    }

    /**
     * Returns the precedence graph of this component.
     *
     * Nodes of the graph are elements of this component. There is an edge from
     * element a to element b when a belongs to the closure of b.
     *
     * The rule a -> a isn't added to the precedence graph
     *
     * When precedence graph is acyclic, then this component is a reduced one.
     *
     * @return the precedence graph
     */
    public ConcreteDGraph<Comparable, ?> precedenceGraph() {
        // compute a TreeMap of closures for each element of the component
        TreeMap<Comparable, TreeSet<Comparable>> closures = new TreeMap<Comparable, TreeSet<Comparable>>();
        for (Comparable x : this.getSet()) {
            ComparableSet setX = new ComparableSet();
            setX.add(x);
            closures.put(x, this.closure(setX));
        }
        // nodes of the graph are elements
        ConcreteDGraph<Comparable, ?> prec = new ConcreteDGraph<Comparable, Object>();
        TreeMap<Comparable, Node> nodeCreated = new TreeMap<Comparable, Node>();
        for (Comparable x : this.getSet()) {
            Node node = new Node(x);
            prec.addNode(node);
            nodeCreated.put(x, node);
        }
        // edges of the graph are closures containments
        for (Comparable source : this.getSet()) {
            for (Comparable target : this.getSet()) {
                if (!source.equals(target)) {
                    // check if source belongs to the closure of target
                    if (closures.get(target).contains(source)) {
                        prec.addEdge(nodeCreated.get(source), nodeCreated.get(target));
                    }
                }
            }
        }
        return prec;
    }

    /**
     * This function returns all reducible elements.
     *
     * A reducible elements is equivalent by closure to one or more other
     * attributes. Reducible elements are computed using the precedence graph of
     * the closure system. Complexity is in O()
     *
     * @return The map of reductible attributes with their equivalent attributes
     */
    public TreeMap<Object, TreeSet> getReducibleElements() {
        // If you can't remove nodes, put them in the rubbish bin ...
        TreeSet<Node> rubbishBin = new TreeSet<Node>();
        // Initialise a map Red of reducible attributes
        TreeMap<Object, TreeSet> red = new TreeMap();
        // Initialise the precedence graph G of the closure system
        ConcreteDGraph<Comparable, ?> graph = this.precedenceGraph();
        // First, compute each group of equivalent attributes
        // This group will be a strongly connected component on the graph.
        // Then, only one element of each group is skipped, others will be deleted.
        DAGraph<SortedSet<Node<Comparable>>, ?> cfc = graph.getStronglyConnectedComponent();
        for (Node<SortedSet<Node<Comparable>>> node : cfc.getNodes()) {
            // Get list of node of this component
            SortedSet<Node<Comparable>> sCC = node.getContent();
            if (sCC.size() > 1) {
                Node<?> y = sCC.first();
                TreeSet yClass = new TreeSet();
                yClass.add(y.getContent());
                for (Node x : sCC) {
                    if (!x.getContent().equals(y.getContent())) {
                        rubbishBin.add(x); // instead of : graph.removeNode(x);
                        red.put(x.getContent(), yClass);
                    }
                }
            }
        }
        // Next, check if an attribute is equivalent to emptyset
        // i.e. its closure is equal to emptyset closure
        TreeSet<Node> sinks = new TreeSet<Node>(graph.getSinks());
        sinks.removeAll(rubbishBin);
        if (sinks.size() == 1) {
            Node s = sinks.first();
            red.put(s.getContent(), new TreeSet());
            rubbishBin.add(s); // instead of : graph.removeNode(s);
        }
        // Finaly, checking a remaining attribute equivalent to its predecessors or not may reduce more attributes.
        // Check all remaining nodes of graph G
        TreeSet<Node> remainingNodes = new TreeSet<Node>();
        for (Node node : graph.getNodes()) {
            remainingNodes.add(node);
        }
        remainingNodes.removeAll(rubbishBin);
        for (Node x : remainingNodes) {
            // TODO getPredecessorNodes must return an iterator
            SortedSet<Node> predecessors = new TreeSet<Node>(graph.getPredecessorNodes(x));
            predecessors.removeAll(rubbishBin);
            if (predecessors.size() > 1) {
                // Create the closure of x
                TreeSet set = new TreeSet();
                set.add(x.getContent());
                TreeSet closureSet = this.closure(set);
                // Create the closure of predecessors
                TreeSet<Comparable> pred = new TreeSet<Comparable>();
                for (Node node : predecessors) {
                    pred.add((Comparable) node.getContent());
                }
                TreeSet<Comparable> closureP = this.closure(pred);
                // Check the equality of two closures
                if (closureSet.containsAll(closureP) && closureP.containsAll(closureSet)) {
                    red.put(x.getContent(), pred);
                }
            }
        }
        // Finally, return the list of reducible elements with their equivalent attributes.
        return red;
    }
}
