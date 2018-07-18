var config = require('config');
const bgsdk = require('./sdk/bgsdk');

function run() {

    //bgsdk.accounts(config.bitget.access_key).then(console.log);
    //bgsdk.balance(config.bitget.access_key,'390350274889256960').then(console.log);
    //accesskey,account_id,amount,price,symbol,type
    //bgsdk.place(config.bitget.access_key,'390350274889256960','10','0.003','eth_btc','buy-limit');
    //bgsdk.submitcancel(config.bitget.access_key,'403385175360188416');
    //bgsdk.order(config.bitget.access_key,'403385175360188416');
    //bgsdk.matchresults(config.bitget.access_key,'403385175360188416');
    //symbol,types,start_date,end_date,states,size,fromId,direct)
    //bgsdk.matchresultsHistory(config.bitget.access_key,'eth_btc','sell-market','2018-06-01','2018-07-20','submitted','50','403041397436887040','next');
    //bgsdk.orders(config.bitget.access_key,'eth_btc','sell-market','2018-06-01','2018-07-20','submitted','50','','');
    //addressWithdraw,amount,currency,fees
    //bgsdk.withdrawCreate(config.bitget.access_key,'1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi','10','btc','0.001');
    //bgsdk.withdrawCancel(config.bitget.access_key,'261');

    bgsdk.withdrawSelect(config.bitget.access_key,'btc','withdraw','10');




    // bgsdk.kline('iost_btc','1min','1').then(console.log);
    // bgsdk.merged('iost_btc').then(console.log);
    // bgsdk.ticckers().then(console.log);
    // bgsdk.depth('iost_btc','step1').then(console.log);
    // bgsdk.trade('iost_btc').then(console.log);
    // bgsdk.trades('iost_btc','10').then(console.log);
    // bgsdk.detail('iost_btc').then(console.log);
    // bgsdk.symbols().then(console.log);
    // bgsdk.currencys().then(console.log);
    // bgsdk.timestamp().then(console.log);


}

run();