import React, { useState } from "react"
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import SourceForm from "main/components/KanbanPopulator/SourceForm"
import DestinationForm from "main/components/KanbanPopulator/DestinationForm"
import CopyProjectForm from "main/components/KanbanPopulator/CopyProjectForm"
import { useCurrentUser } from "main/utils/currentUser";
import { useBackendMutation } from "main/utils/useBackend";

export default function HomePage() {
  const [source, setSource] = useState({});
  const [destination, setDestination] = useState({});

  const { data: currentUser } = useCurrentUser();

  const sourceObjectToAxiosParams = (data) => ({
    // Stryker disable next-line StringLiteral : get is the default
    method: "GET",
    url: "/api/gh/checkSource",
    params: {
      org: data.srcOrg,
      repo: data.srcRepo,
      projNum: data.srcProj
    }
  });

  const sourceMutation = useBackendMutation(
    sourceObjectToAxiosParams,
    {  onSuccess: (response) => {
        setSource(response);
      }
    },
  );

  const onSubmitSource = async (data) => {
    sourceMutation.mutate(data);
  }

  const destinationObjectToAxiosParams = (data) => ({
    // Stryker disable next-line StringLiteral : get is the default
    method: "GET",
    url: "/api/gh/checkDestination",
    params: {
      org: data.destOrg,
      repo: data.destRepo,
    }
  });

  const destinationMutation = useBackendMutation(
    destinationObjectToAxiosParams,
    {  onSuccess: (response) => {
        setDestination(response);
      }
    },
  );

  const onSubmitDestination = async (data) => {
    destinationMutation.mutate(data);
  }
  
  const onSubmitProjectName = async (data) => {
    console.log(data);
  }

  if (!currentUser.loggedIn) { 
    return (
      <BasicLayout>
      <p>Not logged in. Please login to use the Kanban Populator</p>
      </BasicLayout>
    )
  } 

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Kanban Board Populator</h1>
        <h2>Specify Source Repository</h2>
        <SourceForm onSubmit={onSubmitSource} source={source}/>
        <h2>Specify Destination Repository</h2>
        <DestinationForm onSubmit={onSubmitDestination} destination={destination}/>
        <h2>Populate New Kanban Board</h2>
        <CopyProjectForm onSubmit={onSubmitProjectName}/>
      </div>
    </BasicLayout>
  )
}