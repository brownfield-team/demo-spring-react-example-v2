import { act, render, screen, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import CopyProjectForm from "main/components/KanbanPopulator/CopyProjectForm"

describe(CopyProjectForm, () => {
  it("renders correctly ", async () => {
    const { getByText } = render(<CopyProjectForm />);
    await waitFor(() => expect(getByText(/New Project Name/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Populate Kanban Board/)).toBeInTheDocument());
  });

  it("has Correct Error messsages on missing input", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<CopyProjectForm onSubmit={onSubmit} />));

    userEvent.click(screen.getByRole("button"));

    expect(await screen.findByText(/New Project Name is required/)).toBeInTheDocument();

    expect(onSubmit).not.toBeCalled();
  });

  it("calls the onSubmit callback with valid inputs", async () => {
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