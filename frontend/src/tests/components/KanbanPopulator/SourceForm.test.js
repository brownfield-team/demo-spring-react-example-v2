import { act, render, screen, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import SourceForm from "main/components/KanbanPopulator/SourceForm"

describe(SourceForm, () => {
  it("renders correctly ", async () => {
    const { getByText } = render(<SourceForm />);
    await waitFor(() => expect(getByText(/Source Organization/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Source Repository/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Source Project Number/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Submit Source/)).toBeInTheDocument());
  });

  it("has Correct Error messsages on missing input", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<SourceForm onSubmit={onSubmit} />));

    userEvent.click(screen.getByRole("button"));

    expect(await screen.findByText(/Source Organization is required/)).toBeInTheDocument();
    expect(await screen.findByText(/Source Repository is required/)).toBeInTheDocument();
    expect(await screen.findByText(/Source Project Number is required/)).toBeInTheDocument();

    expect(onSubmit).not.toBeCalled();
  });

  it("calls the onSubmit callback with valid inputs", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<SourceForm onSubmit={onSubmit} />));

    userEvent.type(screen.getByLabelText(/Source Organization/), "Test org");
    userEvent.type(screen.getByLabelText(/Source Repository/), "Test repo");
    userEvent.type(screen.getByLabelText(/Source Project Number/), "9");
    userEvent.click(screen.getByRole("button"));

    await waitFor(() => expect(onSubmit).toBeCalledTimes(1));
    expect(onSubmit.mock.calls[0][0]).toMatchObject({
      srcOrg: "Test org",
      srcProj: "9",
      srcRepo: "Test repo",
    });
  });
});