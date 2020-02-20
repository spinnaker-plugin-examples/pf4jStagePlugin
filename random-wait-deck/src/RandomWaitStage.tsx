import React from 'react';
import {
  ExecutionDetailsSection, ExecutionDetailsTasks,
  IExecutionDetailsSectionProps, IStageConfigProps, IStageTypeConfig
} from '@spinnaker/core';

export function RandomWaitDetails(props: IExecutionDetailsSectionProps & { title: string }) {
    return (
        <ExecutionDetailsSection name={props.name} current={props.current}>
            <div><p>{props.stage.outputs.message}</p></div>
        </ExecutionDetailsSection>
    )
}

export const randomWaitStage: IStageTypeConfig = {
    key: 'randomWait',
    label: `Random Wait`,
    description: 'Stage that waits a random amount of time up to the max inputted',
    component: RandomWaitStage, // stage config
    executionDetailsSections: [RandomWaitDetails, ExecutionDetailsTasks],
};

function setMaxWaitTime(event: React.SyntheticEvent, props: IStageConfigProps) {
    let target = event.target as HTMLInputElement;
    props.updateStageField({'maxWaitTime': target.value});
}

function RandomWaitStage(props: IStageConfigProps) {
    return (
        <div>
            <label>
                Max Time To Wait
                <input value={props.stage.maxWaitTime} onChange={(e) => setMaxWaitTime(e, props)} id="maxWaitTime" />
            </label>
        </div>
    );
}

export namespace RandomWaitDetails {
  export const title = 'randomWait';
}
