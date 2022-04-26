import React from 'react';

import CopyProjectForm from "main/components/KanbanPopulator/CopyProjectForm"

export default {
    title: 'components/KanbanPopulator/CopyProjectForm',
    component: CopyProjectForm
};


const Template = (args) => {
    return (
        <CopyProjectForm {...args} />
    )
};

export const Default = Template.bind({});