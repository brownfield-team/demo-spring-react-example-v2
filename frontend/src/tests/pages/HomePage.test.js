import { fireEvent, render, waitFor } from "@testing-library/react";
import HomePage from "main/pages/HomePage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { apiCurrentUserFixtures }  from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

describe("HomePage tests", () => {

    const axiosMock = new AxiosMockAdapter(axios);
    axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);

    const queryClient = new QueryClient();

    test("renders not logged in message when not logged in", async () => {
        axiosMock.onGet("/api/currentUser").reply(403, {});
        const { getByText } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
        await waitFor(() => expect(getByText("Not logged in. Please login to use the Kanban Populator")).toBeInTheDocument());
    });

    test("renders Source Form when logged in", async () => {
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        const { getByText } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => expect(getByText("Specify Source Repository")).toBeInTheDocument());
        expect(getByText("Specify Destination Repository")).toBeInTheDocument();
        expect(getByText("Populate New Kanban Board")).toBeInTheDocument();
    });

    test("When you fill in the source form and click submit, the right things happens", async () => {
        const expectedSourceInfo = {
            org: "ucsb-cs156-w22",
            repo: "HappierCows",
            projectNum: 1,
            projectId: "PRO_dummy_id",
        };
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/gh/checkSource", { params: { org: "ucsb-cs156-w22", repo: "HappierCows", projNum: "1"} })
            .reply(200, expectedSourceInfo);

        const { getByText, getByLabelText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => expect(getByLabelText("Source Organization")).toBeInTheDocument());
        const sourceOrganizationField = getByLabelText("Source Organization");
        const sourceRepositoryField = getByLabelText("Source Repository");
        const sourceProjectNumberField = getByLabelText("Source Project Number");
        const sourceButton = getByTestId("SourceForm-Submit-Button");

        fireEvent.change(sourceOrganizationField, { target: { value: 'ucsb-cs156-w22' } })
        fireEvent.change(sourceRepositoryField, { target: { value: 'HappierCows' } })
        fireEvent.change(sourceProjectNumberField, { target: { value: '1' } })
        fireEvent.click(sourceButton);

        await waitFor(() => expect(getByText("PRO_dummy_id", {exact: false})).toBeInTheDocument());
    });

    test("When you fill in the source form and click submit, returns 500 error", async () => {
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/gh/checkSource").reply(500);

        const { getByLabelText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => expect(getByLabelText("Source Organization")).toBeInTheDocument());
        const sourceOrganizationField = getByLabelText("Source Organization");
        const sourceRepositoryField = getByLabelText("Source Repository");
        const sourceProjectNumberField = getByLabelText("Source Project Number");
        const sourceButton = getByTestId("SourceForm-Submit-Button");

        fireEvent.change(sourceOrganizationField, { target: { value: 'fakeOrg' } })
        fireEvent.change(sourceRepositoryField, { target: { value: 'fakeRepo' } })
        fireEvent.change(sourceProjectNumberField, { target: { value: '8' } })
        fireEvent.click(sourceButton);

        await waitFor(() => expect(mockToast).toHaveBeenCalledTimes(2));
        expect(mockToast.mock.calls[0][0]).toEqual("Axios Error: Error: Request failed with status code 500");
        expect(mockToast.mock.calls[1][0]).toEqual("Error: Request failed with status code 500");
    });

    test("When you fill in form the destination form and click submit, the right things happens", async () => {
        const expectedDestinationInfo = {
            org: "ucsb-cs156-w22",
            repo: "HappierCows",
            repositoryId: "R_dummy_id"
        };
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/gh/checkDestination", { params: { org: "ucsb-cs156-w22", repo: "HappierCows"} }).reply(200, expectedDestinationInfo);

        const { getByText, getByLabelText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => expect(getByLabelText("Destination Organization")).toBeInTheDocument());
        const destinationOrganizationField = getByLabelText("Destination Organization");
        const destinationRepositoryField = getByLabelText("Destination Repository");
        const destinationButton = getByTestId("DestinationForm-Submit-Button");


        fireEvent.change(destinationOrganizationField, { target: { value: 'ucsb-cs156-w22' } })
        fireEvent.change(destinationRepositoryField, { target: { value: 'HappierCows' } })
        fireEvent.click(destinationButton);

        await waitFor(() => expect(getByText("R_dummy_id", {exact: false})).toBeInTheDocument());
    });

    test("When you fill in the destination form and click submit, returns 500 error", async () => {
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/gh/checkDestination", { params: { org: "fakeOrg", repo: "fakeRepo" } }).reply(500);

        const { getByLabelText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => expect(getByLabelText("Destination Organization")).toBeInTheDocument());
        const destinationOrganizationField = getByLabelText("Destination Organization");
        const destinationRepositoryField = getByLabelText("Destination Repository");
        const destinationButton = getByTestId("DestinationForm-Submit-Button");

        fireEvent.change(destinationOrganizationField, { target: { value: 'fakeOrg' } })
        fireEvent.change(destinationRepositoryField, { target: { value: 'fakeRepo' } })
        fireEvent.click(destinationButton);

        await waitFor(() => expect(mockToast).toHaveBeenCalledTimes(2));
        expect(mockToast.mock.calls[0][0]).toEqual("Axios Error: Error: Request failed with status code 500");
        expect(mockToast.mock.calls[1][0]).toEqual("Error: Request failed with status code 500");
    });

    test("When you fill in form and click submit, the right things happens project name", async () => {
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);

        const consoleLogMock = jest.spyOn(console, 'log').mockImplementation();

        const { getByLabelText, getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <HomePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => expect(getByLabelText("New Project Name")).toBeInTheDocument());
        const projectNameField = getByLabelText("New Project Name");
        const copyProjectButton = getByTestId("CopyProjectForm-Submit-Button");

        fireEvent.change(projectNameField, { target: { value: 'Test project name' } })
        fireEvent.click(copyProjectButton);


        const expectedProjectInfo = {
            projName: "Test project name",
        };

        await waitFor(() => expect(consoleLogMock).toHaveBeenCalledTimes(1));
        expect(console.log.mock.calls[0][0]).toEqual(expectedProjectInfo);

        consoleLogMock.mockRestore();
    });

});


