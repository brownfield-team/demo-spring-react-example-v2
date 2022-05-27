import { act, render, screen, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import CopyProjectForm from "main/components/KanbanPopulator/CopyProjectForm"

describe(CopyProjectForm, () => {
  it("renders correctly ", async () => {
    const { getByText } = render(<CopyProjectForm />);
    await waitFor(() => expect(getByText(/New Project Name/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Populate Kanban Board/)).toBeInTheDocument());
  });

  it("calls the onSubmit callback with any input", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<CopyProjectForm onSubmit={onSubmit} />));

    userEvent.type(screen.getByLabelText(/New Project Name/), "Test proj name");
    userEvent.click(screen.getByRole("button"));

    await waitFor(() => expect(onSubmit).toBeCalledTimes(1));
    expect(onSubmit.mock.calls[0][0]).toMatchObject({
      projName: "Test proj name",
    });
  });
});