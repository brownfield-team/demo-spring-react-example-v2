import { act, render, screen, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import DestinationForm from "main/components/KanbanPopulator/DestinationForm"

describe(DestinationForm, () => {
  it("renders correctly ", async () => {
    const { getByText } = render(<DestinationForm />);
    await waitFor(() => expect(getByText(/Destination Organization/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Destination Repository/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Submit Destination/)).toBeInTheDocument());
  });

  it("has Correct Error messsages on missing input", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<DestinationForm onSubmit={onSubmit} />));

    userEvent.click(screen.getByRole("button"));

    expect(await screen.findByText(/Destination Organization is required/)).toBeInTheDocument();
    expect(await screen.findByText(/Destination Repository is required/)).toBeInTheDocument();

    expect(onSubmit).not.toBeCalled();
  });

  it("calls the onSubmit callback with valid inputs", async () => {
    const onSubmit = jest.fn();
    await act(async () => render(<DestinationForm onSubmit={onSubmit} />));

    userEvent.type(screen.getByLabelText(/Destination Organization/), "Test org");
    userEvent.type(screen.getByLabelText(/Destination Repository/), "Test repo");
    userEvent.click(screen.getByRole("button"));

    await waitFor(() => expect(onSubmit).toBeCalledTimes(1));
    expect(onSubmit.mock.calls[0][0]).toMatchObject({
      destOrg: "Test org",
      destRepo: "Test repo",
    });
  });
});