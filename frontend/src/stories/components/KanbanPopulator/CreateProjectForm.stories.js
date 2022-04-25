import React from 'react';

import CreateProjectForm from "main/components/KanbanPopulator/CreateProjectForm"

export default {
    title: 'components/KanbanPopulator/CreateProjectForm',
    component: CreateProjectForm
};


const Template = (args) => {
    return (
        <CreateProjectForm {...args} />
    )
};

export const Default = Template.bind({});