package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayGraph;
import wooteco.subway.domain.fare.SubwayFare;
import wooteco.subway.domain.fare.vo.Age;
import wooteco.subway.domain.path.ShortestPath;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.request.PathsRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse showPaths(PathsRequest pathsRequest) {
        List<Section> sections = sectionRepository.findAll();
        Station source = stationRepository.findById(pathsRequest.getSource());
        Station target = stationRepository.findById(pathsRequest.getTarget());
        Age age = new Age(pathsRequest.getAge());
        return getPathResponse(sections, source, target, age);
    }

    private PathResponse getPathResponse(
            List<Section> sections, Station source, Station target, Age age
    ) {
        SubwayGraph subwayGraph = new SubwayGraph(sections);
        ShortestPath shortestPath = subwayGraph.getShortestPath(source, target);

        List<Station> shortestRoute = shortestPath.getShortestRoute();
        int distance = shortestPath.getShortestDistance();
        SubwayFare fare = subwayGraph.getFare(source, target);
        return new PathResponse(toStationResponse(shortestRoute), distance, fare.calculate(age));
    }

    private List<StationResponse> toStationResponse(List<Station> route) {
        return route.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

}

