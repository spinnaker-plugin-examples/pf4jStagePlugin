import nodeResolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import typescript from 'rollup-plugin-typescript';
import postCss from 'rollup-plugin-postcss';
import externalGlobals from 'rollup-plugin-external-globals';

export default [
  {
    input: 'src/index.ts',
    plugins: [
      nodeResolve(),
      commonjs(),
      typescript(),
      // map imports from shared libraries (react, etc) to global variables exposed by spinnaker
      externalGlobals(spinnakerSharedLibraries()),
      // import from .css, .less, and inject into the document <head></head>
      postCss(),
    ],
    output: [{ dir: 'build/dist', format: 'es', }]
  }
];

/*
Pulls dependencies from Spinnaker. The `libraries` constant is a list of libraries that make the plugin work correctly.
Items in this list must be from the shared libraries (https://github.com/spinnaker/deck/blob/master/app/scripts/modules/core/src/plugins/sharedLibraries.ts#L32) exposed to plugin creators.
 */
function spinnakerSharedLibraries() {
  const libraries = ['lodash', 'react', 'react-dom', '@spinnaker/core'];

  function getGlobalVariable(libraryName) {
    const prefix = 'spinnaker.plugins.sharedLibraries';
    const sanitizedLibraryName = libraryName.replace(/[^a-zA-Z0-9_]/g, '_');
    return `${prefix}.${sanitizedLibraryName}`;
  }

  return libraries.reduce((globalsMap, libraryName) => {
    return { ...globalsMap, [ libraryName ]: getGlobalVariable(libraryName) }
  }, {});
}
