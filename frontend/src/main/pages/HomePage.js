import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import SourceForm from "main/components/KanbanPopulator/SourceForm"
import DestinationForm from "main/components/KanbanPopulator/DestinationForm"
import CopyProjectForm from "main/components/KanbanPopulator/CopyProjectForm"
import { useCurrentUser } from "main/utils/currentUser";

export default function HomePage() {

  const { data: currentUser } = useCurrentUser();

  if (!currentUser.loggedIn) {
    return (
      <BasicLayout>
      <p>Not logged in. Please login to use the Kanban Populator</p>
      </BasicLayout>
    )
  } 

  const onSubmitSource = async (data) => {
    console.log(data);
  }

  const onSubmitDestination = async (data) => {
    console.log(data);
  }

  const onSubmitProjectName = async (data) => {
    console.log(data);
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Kanban Board Populator</h1>
        <h2>Specify Source Repository</h2>
        <SourceForm onSubmit={onSubmitSource}/>
        <h2>Specify Destination Repository</h2>
        <DestinationForm onSubmit={onSubmitDestination}/>
        <h2>Populate New Kanban Board</h2>
        <CopyProjectForm onSubmit={onSubmitProjectName}/>
      </div>
    </BasicLayout>
  )
}