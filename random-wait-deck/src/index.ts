import { IDeckPlugin } from '@spinnaker/core';
import { randomWaitStage } from './RandomWaitStage';

export const plugin: IDeckPlugin = {
  stages: [randomWaitStage],
};
