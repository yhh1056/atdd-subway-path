package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Edges;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.exception.WrongPathException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(PathRequest request) {
        Lines wholeLines = new Lines(lineRepository.findAll());
        Stations wholeStations = new Stations(stationRepository.findAll());
        Edges wholeEdges = wholeLines.findWholeEdges();

        Long sourceStationId = request.getSource();
        Long targetStationId = request.getTarget();

        validate(wholeStations, sourceStationId, targetStationId);

        List<Long> stationIdsInShortestPath
                = wholeLines.createShortestPath(sourceStationId, targetStationId, request.getType());

        Stations stationsInShortestPath = stationIdsInShortestPath.stream()
                .map(wholeStations::findStationById)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));

        Edges edgesInShortestPath = wholeEdges.extractPathEdges(stationIdsInShortestPath);
        return new PathResponse(
                stationsInShortestPath.getStations(),
                edgesInShortestPath.getDistance(),
                edgesInShortestPath.getDuration()
        );
    }

    private void validate(Stations wholeStations, Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new WrongPathException();
        }
        if (wholeStations.isNotContains(sourceStationId) || wholeStations.isNotContains(targetStationId)) {
            throw new StationNotFoundException();
        }
    }
}