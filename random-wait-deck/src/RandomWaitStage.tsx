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

/*
Define Spinnaker Stages with IStageTypeConfig.
 Required options: https://github.com/spinnaker/deck/blob/abac63ce5c88b809fcf5ed1509136fe96489a051/app/scripts/modules/core/src/domain/IStageTypeConfig.ts
- label -> The name of the Stage
- description -> Long form that describes what the Stage actually does
- key -> A unique name for the Stage
- component -> The rendered React component
 */
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

/*
 IStageConfigProps defines properties passed to all Spinnaker Stages.
 See IStageConfigProps.ts (https://github.com/spinnaker/deck/blob/master/app/scripts/modules/core/src/pipeline/config/stages/common/IStageConfigProps.ts) for a complete list of properties.
 Pass a JSON object to the `updateStageField` method to add the `maxWaitTime` to the Stage.

 This method returns JSX (https://reactjs.org/docs/introducing-jsx.html) that gets displayed in the Spinnaker UI.
 */
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
