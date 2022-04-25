import { act, render, screen, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import CreateProjectForm from "main/components/KanbanPopulator/CreateProjectForm"

describe(CreateProjectForm, () => {
  it("renders correctly ", async () => {
    const { getByText } = render(<CreateProjectForm />);
    await waitFor(() => expect(getByText(/New Project Name/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Create New Project/)).toBeInTheDocument());
  });

  it("has Correct Error messsages on missing input", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<CreateProjectForm onSubmit={onSubmit} />));

    userEvent.click(screen.getByRole("button"));

    expect(await screen.findByText(/New Project Name is required/)).toBeInTheDocument();

    expect(onSubmit).not.toBeCalled();
  });

  it("calls the onSubmit callback with valid inputs", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<CreateProjectForm onSubmit={onSubmit} />));

    userEvent.type(screen.getByLabelText(/New Project Name/), "Test proj name");
    userEvent.click(screen.getByRole("button"));

    await waitFor(() => expect(onSubmit).toBeCalledTimes(1));
    expect(onSubmit.mock.calls[0][0]).toMatchObject({
      projName: "Test proj name",
    });
  });
});