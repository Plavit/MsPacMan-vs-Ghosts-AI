package game.controllers.pacman.exercises.e5.search.base;

import java.util.Collection;

import game.controllers.pacman.exercises.e5.graph.IGraph;
import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.exercises.e5.graph.INode;
import game.controllers.pacman.exercises.e5.graph.maze.MazeGraph;
import game.controllers.pacman.exercises.e5.graph.maze.MazeGraphNode;
import game.controllers.pacman.exercises.e5.search.InformedNode;

/**
 * Incremental path-finder.
 * 
 * The idea is to have a path-finder whose execution can be spread between multiple game frames.
 * 
 * Therefore we do not have single method that performs the path-finding but we have a stateful object.
 * 
 * Path finder always needs to be {@link #init(MazeGraph, MazeGraphNode, MazeGraphNode)}ed first, then you can
 * call {@link #step()} until path-finder reaches one of its terminal {@link PathFinderState} that
 * is  {@link PathFinderState#PATH_FOUND} or {@link PathFinderState#PATH_NOT_FOUND}.
 *  
 * @author Jimmy
 */
public interface IPathFinder<NODE extends INode, LINK extends ILink<NODE>, SEARCH_NODE extends InformedNode, PATH_FINDER_CONFIG> {
	
	/**
	 * Shorthand of the Path-Finder name (e.g. DFS, BFS, UCS, ...)
	 * @return
	 */
	public String getName();
	
	// ==============
	// INITIALIZATION
	// ==============
	
	/**
	 * Wipes {@link IPathFinder} internal data.
	 * 
	 * Reset {@link #getState()} to {@link PathFinderState#INIT}.
	 */
	public void reset();
	
	/**
	 * Initialize {@link IPathFinder} to use {@link MazeGraph} and prepare to search from 'start' towards 'goal' node.
	 * 
	 * Changes state of {@link #getState()} to {@link PathFinderState#RUNNING}.
	 * 
	 * @param graph how does the graph we want to search in looks like?
	 * @param goal
	 * @param strategy
	 * @param view
	 * @param path-finder specific config
	 */
	public void init(IGraph<NODE, LINK> graph, ISearchGoal<NODE, LINK, SEARCH_NODE> goal, ISearchStrategy<NODE, LINK, SEARCH_NODE> strategy, IGraphView<NODE, LINK, SEARCH_NODE> view, PATH_FINDER_CONFIG config);
		
	// =========
	// EXECUTION
	// =========
	
	/**
	 * What state the path-finder is in.
	 * @return
	 */
	public PathFinderState getState();
	
	/**
	 * Perform another step of "IPathFinder".
	 * 
	 * Can be called if and only if {@link #getState()} == {@link PathFinderState#RUNNING}.
	 * 
	 * @return what state the path-finder is in after the method finishes
	 */
	public PathFinderState step();
	
	// =======
	// RESULTS
	// =======
	
	/**
	 * Returns found path that has 'start' node as the first element and 'goal' as the last element of the path.
	 * 
	 * Can be called if and only if {@link #getState()} == {@link PathFinderState#PATH_FOUND}.
	 *  
	 * @return
	 */
	public Path<NODE, LINK> getPath();
	
	// ==================
	// INTERMEDIATE STATE
	// ==================
	
	/**
	 * Returns START node.
	 * @return
	 */
	public NODE getStart();
	
	/**
	 * Returns GOAL node.
	 * @return
	 */
	public NODE getGoal();
	
	/**
	 * Returns current parent-on-the-path-from START
	 * @param node
	 * @return
	 */
	public NODE getParent(NODE node);
	
	/**
	 * Returns extra information about the search node.
	 * @param node
	 * @return
	 */
	public SEARCH_NODE getSearchNode(NODE node);
	
	/**
	 * Already expanded nodes that were discussed.
	 * @return
	 */
	public Collection<SEARCH_NODE> getClosedList();
	
	/**
	 * Containing "leaf nodes", also known as FRINGE.
	 * @return
	 */
	public Collection<SEARCH_NODE> getOpenList();
	
	// =========
	// PROFILING
	// =========
	
	/**
	 * How many number of iterations we have performed so-far.
	 * How many times {@link #step()} was called.
	 * @return
	 */
	public int getSteps();
	
}
