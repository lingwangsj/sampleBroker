import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WlanMySuffix } from './wlan-my-suffix.model';
import { WlanMySuffixService } from './wlan-my-suffix.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-wlan-my-suffix',
    templateUrl: './wlan-my-suffix.component.html'
})
export class WlanMySuffixComponent implements OnInit, OnDestroy {
wlans: WlanMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private wlanService: WlanMySuffixService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.wlanService.query().subscribe(
            (res: ResponseWrapper) => {
                this.wlans = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWlans();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WlanMySuffix) {
        return item.id;
    }
    registerChangeInWlans() {
        this.eventSubscriber = this.eventManager.subscribe('wlanListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
