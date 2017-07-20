const path = require('path');
const baseDirectory = `${process.cwd()}${path.sep}node_modules${path.sep}`;

const runCli = require(`${baseDirectory}roc/lib/cli/runCli`).default;
const commands = require(`${baseDirectory}roc/lib/commands`).default;
const buildDocumentationObject = require(`${baseDirectory}roc/lib/documentation/buildDocumentationObject`).default;

commands['roc-config'] = (roc) => {
    const context = roc.context;
    const extensionSettings = context.extensionConfig.settings;
    const metaSettings = context.meta.settings;

    const result = buildDocumentationObject(extensionSettings, metaSettings);
    console.log(JSON.stringify(result, null, 4));
};

runCli({commands});
