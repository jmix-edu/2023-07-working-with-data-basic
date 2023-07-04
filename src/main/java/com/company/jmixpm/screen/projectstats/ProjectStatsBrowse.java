package com.company.jmixpm.screen.projectstats;

import com.company.jmixpm.app.ProjectStatsService;
import com.company.jmixpm.entity.ProjectStats;
import io.jmix.core.LoadContext;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("ProjectStats.browse")
@UiDescriptor("project-stats-browse.xml")
@LookupComponent("projectStatsesTable")
public class ProjectStatsBrowse extends StandardLookup<ProjectStats> {

    private ProjectStatsService projectStatsService;

    @Autowired
    public void setProjectStatsService(ProjectStatsService projectStatsService) {
        this.projectStatsService = projectStatsService;
    }

    @Install(to = "projectStatsesDl", target = Target.DATA_LOADER)
    private List<ProjectStats> projectStatsesDlLoadDelegate(LoadContext<ProjectStats> loadContext) {
        // Here you can load entities from an external store
        return projectStatsService.load();
    }
}