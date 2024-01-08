# Retranslation Plugin Documentation

## Overview

The `Retranslation` plugin is a **JOKE** Spigot plugin for Minecraft that translates player chat messages using an external web API. It supports language translation between various languages. Inspired by Japanese Youtuber KUNの五十人クラフト. (https://www.youtube.com/watch?v=ejizZhoQfyY)

***THIS PLUGIN IS JOKE PLUGIN***

## Plugin Features

- Translation of player chat messages.
- Configurable source and target languages.
- Enable/disable translation.
- Change the web API URL for translation.

## Configuration

The plugin can be configured using the `config.yml` file. The default configuration is as follows:

```yaml
enabled: 0
lang_from: "en"
lang_to: "ja"
api_url: "API_URL/exec?text="
```

- enabled: 1 to enable translation, 0 to disable.
- lang_from: Source language code (e.g., "en" for English).
- lang_to: Target language code (e.g., "ja" for Japanese).
- api_url: URL of the web API for translation.

## Web API Requirements

The web API should return a JSON object with the following structure:

```json
{
  "result": "Translated message",
  "error": null
}
```
result: The translated message.
error: An optional field for indicating errors. If present, the plugin will handle errors gracefully.
Web API URL Format

The web API URL should follow the format:

```
https://example.com/api?text=<url-encoded-message>&source=<source-language>&target=<target-language>
```

<url-encoded-message>: The URL-encoded original message to be translated.

<source-language>: The source language code.

<target-language>: The target language code.

Ensure that the web API supports the specified source and target languages.

## Commands

The plugin provides the following commands:

### /retranslate enable
- Enables the translation feature.
- Requires operator (OP) permissions.
### /retranslate disable
- Disables the translation feature.
- Requires operator (OP) permissions.
### /retranslate lang_from <language>
- Changes the source language for translation.
- Supported languages: "en", "ja", "ru", "zh", "is", "de", "it", "fr", "ko".
- Requires operator (OP) permissions.
### /retranslate lang_to <language>
- Changes the target language for translation.
- Supported languages: "en", "ja", "ru", "zh", "is", "de", "it", "fr", "ko".
- Requires operator (OP) permissions.
### /retranslate api_url <url>
- Changes the web API URL for translation.
- Requires operator (OP) permissions.


### Plugin Usage

Install the plugin.
Configure the config.yml file as needed.
Use the provided commands to manage translation settings.
