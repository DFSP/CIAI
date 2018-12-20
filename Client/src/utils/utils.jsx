export function timedFetch(url, options, timeout = 10000) {
    return new Promise((resolve, reject) => {
        fetch(url, options).then(resolve).catch(reject);
        if (timeout) {
            const e = new Error("A conexão excedeu o limite de tempo.");
            setTimeout(reject, timeout, e);
        }
    });
}
