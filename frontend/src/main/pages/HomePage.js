import React, { useState } from "react"
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import SourceForm from "main/components/KanbanPopulator/SourceForm"
import DestinationForm from "main/components/KanbanPopulator/DestinationForm"
import { useCurrentUser } from "main/utils/currentUser";
import { useBackendMutation } from "main/utils/useBackend";

export default function HomePage() {
  const [source, setSource] = useState({});
  const [destination, setDestination] = useState({});

  const { data: currentUser } = useCurrentUser();

  const onSuccess = (response) => {
    setSource({
      org: response.org,
      repo: response.repo,
      projNum: response.projectNum,
      projectId: response.projectId
    });
  }

  const sourceObjectToAxiosParams = (data) => ({
    method: "GET",
    url: "/api/gh/checkSource",
    params: {
      org: data.org,
      repo: data.repo,
      projNum: data.proj
    }
  });

  const sourceMutation = useBackendMutation(
    sourceObjectToAxiosParams,
    { onSuccess },
  );

  const onSubmitSource = async (data) => {
    console.log(destination);
    sourceMutation.mutate(data);
  }

  onSuccess = (response) => {
    if(response.success){
      console.log(response);
      setDestination({
        org: response.org,
        repo: response.repo,
        repositoryId: response.repositoryId
      });
    }
    else{
      const errorMessage = `Error Checking Destination. Ensure Organization, Repository and Project Number are all valid`;
      toast(errorMessage);
    }
  }

  const destinationObjectToAxiosParams = (data) => ({
    method: "GET",
    url: "/api/gh/checkDestination",
    params: {
      org: data.org,
      repo: data.repo,
    }
  });

  const destinationMutation = useBackendMutation(
    destinationObjectToAxiosParams,
    { onSuccess },
  );

  const onSubmitDestination = async (data) => {
    console.log(source);
    destinationMutation.mutate(data);
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
        <h2>Specify Source</h2>
        <SourceForm onSubmit={onSubmitSource} source={source}/>
        <h2>Specify Destination and new Kanban Board Name</h2>
        <DestinationForm onSubmit={onSubmitDestination}/>
      </div>
    </BasicLayout>
  )
}