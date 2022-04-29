import React, { useState } from "react"
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import SourceForm from "main/components/KanbanPopulator/SourceForm"
import DestinationForm from "main/components/KanbanPopulator/DestinationForm"
import { useCurrentUser } from "main/utils/currentUser";
import { useBackendMutation } from "main/utils/useBackend";

export default function HomePage() {
  const [source, setSource] = useState({});

  const { data: currentUser } = useCurrentUser();

  const onSuccess = (response) => {
    setSource({
      org: response.org,
      repo: response.repo,
      projNum: response.projectNum,
      projectId: response.projectId
    });
  }

  const objectToAxiosParams = (data) => ({
    // Stryker disable next-line StringLiteral : get is the default
    method: "GET",
    url: "/api/gh/checkSource",
    params: {
      org: data.org,
      repo: data.repo,
      projNum: data.proj
    }
  });

  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
  );

  const onSubmitSource = async (data) => {
    mutation.mutate(data);
  }

  const onSubmitDestination = async (data) => {
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
        <h2>Specify Source</h2>
        <SourceForm onSubmit={onSubmitSource} source={source}/>
        <h2>Specify Destination and new Kanban Board Name</h2>
        <DestinationForm onSubmit={onSubmitDestination}/>
      </div>
    </BasicLayout>
  )
}