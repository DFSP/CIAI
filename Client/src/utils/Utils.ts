export function timedFetch(url: string, options: any, timeout = 10000) {
    return new Promise((resolve, reject) => {
        fetch(url, options).then(resolve).catch(reject);
        if (timeout) {
            const e = new Error("A conexão excedeu o limite de tempo.");
            setTimeout(reject, timeout, e);
        }
    });
}

export function fetchUrl(url: string, method: string, body: any,
                         successMessage: string, callback: (s: boolean, c: boolean) => void) {
    fetch(url, {
        method,
        body,
        headers: new Headers({
            'Authorization': 'Basic '+btoa('admin:password'),
            'Content-type': 'application/json;charset=UTF-8'
        }),
    })
        .then(response => {
            if (response.ok) {
                // this.props.changeModalStatus(false);
                callback(false, false);
              /*  alert(successMessage);*/
            } else {
                throw new Error(response.statusText);
            }
        }).catch((e: string) => console.log(e));
}
