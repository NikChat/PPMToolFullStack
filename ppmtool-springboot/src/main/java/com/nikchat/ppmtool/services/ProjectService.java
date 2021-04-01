package com.nikchat.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikchat.ppmtool.domain.Backlog;
import com.nikchat.ppmtool.domain.Project;
import com.nikchat.ppmtool.domain.User;
import com.nikchat.ppmtool.exceptions.ProjectIdException;
import com.nikchat.ppmtool.repositories.BacklogRepository;
import com.nikchat.ppmtool.repositories.ProjectRepository;
import com.nikchat.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private BacklogRepository backlogRepository;
    
    @Autowired
    private UserRepository userRepository;
    

    public Project saveOrUpdateProject(Project project, String username){
        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            
            if(project.getId()==null){ // When creating new project, create a new backlog for it
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null){ // When updating an existing project
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            
            return projectRepository.save(project);
            
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }

    }
    

    public Project findProjectByIdentifier(String projectId){
    	
    	//Only want to return the project if the user looking for it is the owner

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' does not exist");

        }

        return project;
    }
    
    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }
    
    
    public void deleteProjectByIdentifier(String projectid){
        Project project = projectRepository.findByProjectIdentifier(projectid.toUpperCase());

        if(project == null){
            throw  new  ProjectIdException("Cannot Delete Project with ID '"+projectid+"'. This project does not exist");
        }

        projectRepository.delete(project);
    }

}
