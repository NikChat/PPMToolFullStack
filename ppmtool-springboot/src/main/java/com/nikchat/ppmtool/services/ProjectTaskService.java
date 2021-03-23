package com.nikchat.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikchat.ppmtool.domain.Backlog;
import com.nikchat.ppmtool.domain.ProjectTask;
import com.nikchat.ppmtool.repositories.BacklogRepository;
import com.nikchat.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //Exceptions: Project not found

        //ProjectTasks to be added to a specific project, project != null, Backlog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the backlog to projectTask
        projectTask.setBacklog(backlog);
        //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
        Integer backlogSequence = backlog.getPTSequence();
        // Update the Backlog SEQUENCE
        backlogSequence++;
        backlog.setPTSequence(backlogSequence);
        
        //Add Sequence to ProjectTask
        projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence); // backlog.getProjectIdentifier()
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null
        if(projectTask.getPriority()==null || projectTask.getPriority()==0){ // we need projectTask.getPriority()== 0 to handle the form
            projectTask.setPriority(3);
        }
        
        //INITIAL status when status is null
        if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }
}

