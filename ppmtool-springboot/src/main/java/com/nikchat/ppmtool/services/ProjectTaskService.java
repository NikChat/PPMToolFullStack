package com.nikchat.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikchat.ppmtool.domain.Backlog;
import com.nikchat.ppmtool.domain.Project;
import com.nikchat.ppmtool.domain.ProjectTask;
import com.nikchat.ppmtool.exceptions.ProjectNotFoundException;
import com.nikchat.ppmtool.repositories.BacklogRepository;
import com.nikchat.ppmtool.repositories.ProjectRepository;
import com.nikchat.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

    	
    		//ProjectTasks to be added to a specific project, project != null, Backlog exists
            Backlog backlog =  projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
            //set the backlog to projectTask
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
            Integer backlogSequence = backlog.getPTSequence();
            // Update the Backlog SEQUENCE
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            
            //Add Sequence to ProjectTask
            projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+backlogSequence); // backlog.getProjectIdentifier()
            projectTask.setProjectIdentifier(backlog.getProjectIdentifier());

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
    

    public Iterable<ProjectTask> findBacklogById(String projectIdentifier, String username){
    	
    	projectService.findProjectByIdentifier(projectIdentifier, username);
    	
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }
    
    
    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){

    	//make sure we are searching on an existing backlog
    	projectService.findProjectByIdentifier(backlog_id, username);

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id.toUpperCase())){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id.toUpperCase());
        }

        return projectTask;
    }
    
    
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        
    	ProjectTask projectTask = this.findPTByProjectSequence(backlog_id, pt_id, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }
    
    
    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
    	
    	ProjectTask projectTask = this.findPTByProjectSequence(backlog_id, pt_id, username);

        projectTaskRepository.delete(projectTask);
    }
    
}

