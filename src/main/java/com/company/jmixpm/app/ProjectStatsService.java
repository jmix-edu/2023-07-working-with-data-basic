package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.ProjectStats;
import com.company.jmixpm.entity.Task;
import io.jmix.core.DataManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProjectStatsService {


    private final DataManager dataManager;

    public ProjectStatsService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<ProjectStats> load() {
        List<Project> projectList = dataManager.load(Project.class)
                .all()
                .fetchPlan("project-with-tasks")
                .list();

        List<ProjectStats> statsList = projectList.stream()
                .map(project -> {
                    ProjectStats projectStats = dataManager.create(ProjectStats.class);
                    projectStats.setProjectName(project.getName());
                    projectStats.setTasksCount(project.getTasks().size());

                    Integer sum = project.getTasks().stream()
                            .map(Task::getEstimatedEfforts)
                            .reduce(0, Integer::sum);

                    projectStats.setPlannedEfforts(sum);
                    projectStats.setActualEfforts(getActualEfforts(project.getId()));
                    return projectStats;
                })
                .collect(Collectors.toList());

        return statsList;
    }

    public Integer getActualEfforts(UUID projectId) {
        return dataManager.loadValue(
                "select sum(t.timeSpent) from TimeEntry t where t.task.project.id = :projectId", Integer.class)
                .parameter("projectId", projectId)
                .one();
    }

}