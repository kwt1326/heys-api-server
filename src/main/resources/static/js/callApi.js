function simpleQsUrl(url, params) {
  return url + (Object.keys(params ?? {}).length > 0
    ? `?${Object.keys(params)
        .map((key) => `${key}=${params[key]}`)
        .join("&")}`
    : "");
}

/**
 * @param {*} options url, method, urlParams, body, headers, onSuccess, onError
 */
function callApi(options) {
  if (!options?.url) {
    return console.error("[Error] url is required");
  }
  if (!options?.method) {
    return console.error("[Error] method is required");
  }

  const xhr = new XMLHttpRequest();
  xhr.open(options.method, simpleQsUrl(options.url, options.urlParams), true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4) {
      if (xhr.status == 200) {
        const data = JSON.parse(xhr.responseText);
        return options.onSuccess && options.onSuccess(data || {});
      }
      console.error(`[Error] status: ${xhr.status} message: ${xhr.statusText}`);
      return options.onError && options.onError(xhr);
    }
  };

  if (options.headers) {
    for (const key in options.headers) {
      xhr.setRequestHeader(key, options.headers[key]);
    }
  }

  const data = JSON.stringify(options.body ?? {});
  xhr.send(options.method !== 'GET' ? data : undefined);
}

function post(params) {
  return callApi({
    ...params,
    method: "POST",
  });
}

function get(params) {
  return callApi({
    ...params,
    method: "GET",
  });
}

function put(params) {
  return callApi({
    ...params,
    method: "PUT",
  });
}

function del(params) {
  return callApi({
    ...params,
    method: "DELETE",
  });
}
