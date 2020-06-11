package wooteco.subway.admin.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import wooteco.subway.admin.domain.CustomException;
import wooteco.subway.admin.domain.LineStation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PathGraph {
    private ShortestPathAlgorithm<Long, WeightedEdge> graph;
    private PathType pathType;

    public PathGraph(ShortestPathAlgorithm<Long, WeightedEdge> graph, PathType pathType) {
        this.graph = graph;
        this.pathType = pathType;
    }

    public static WeightedGraph<Long, WeightedEdge> getGraph(List<LineStation> lineStations, PathType pathType) {
        WeightedGraph<Long, WeightedEdge> graph
                = new DirectedWeightedMultigraph<>(WeightedEdge.class);

        for (LineStation station : lineStations) {
            graph.addVertex(station.getStationId());
            if (station.isFirst()) {
                continue;
            }
            WeightedEdge weightedEdge = new WeightedEdge(station, pathType);
            graph.addEdge(station.getPreStationId(), station.getStationId(), weightedEdge);
            graph.setEdgeWeight(weightedEdge, pathType.findWeight(station));
        }
        return graph;
    }

    public ShortestPathAlgorithm<Long, WeightedEdge> getTotalPath() {
        return graph;
    }

    private Optional<GraphPath<Long, WeightedEdge>> getPath(Long source, Long target) {
        try {
            return Optional.ofNullable(graph.getPath(source, target));
        } catch (IllegalArgumentException e) {
            throw new CustomException("경로가 존재하지 않습니다.", e);
        }
    }

    public Path createPath(Long source, Long target) {
        try {
            return new Path(getPath(source, target).get(), pathType);
        } catch (NoSuchElementException e) {
            throw new CustomException("존재하지 않는 경로입니다.", e);
        }
    }
}