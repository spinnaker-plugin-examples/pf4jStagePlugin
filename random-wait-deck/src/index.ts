import { IDeckPlugin } from '@spinnaker/core';
import { randomWaitStage } from './RandomWaitStage';
import { initialize } from './initialize';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [randomWaitStage],
};
