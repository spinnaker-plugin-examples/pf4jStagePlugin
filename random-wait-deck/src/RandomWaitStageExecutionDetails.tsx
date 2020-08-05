import React from 'react';

import {
  ExecutionDetailsSection,
  IExecutionDetailsSectionProps,
} from '@spinnaker/core';

/* 
 * You can use this component to provide information to users about
 * how the stage was configured and the results of its execution.
 *
 * In general, you will access two properties of `props.stage`:
 * - `props.stage.outputs` maps to your SimpleStage's `Output` class.
 * - `props.stage.context` maps to your SimpleStage's `Context` class.
 */
export function RandomWaitStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <div>
        <p>Waited {props.stage.outputs.timeToWait} second(s)</p>
      </div>
    </ExecutionDetailsSection>
  );
}

// The title here will be used as the tab name inside the
// pipeline stage execution view. Camel case will be mapped 
// to space-delimited text: randomWait -> Random Wait.
export namespace RandomWaitStageExecutionDetails {
  export const title = 'randomWait';
}
