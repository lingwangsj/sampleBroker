import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WlanGroupMySuffix } from './wlan-group-my-suffix.model';
import { WlanGroupMySuffixService } from './wlan-group-my-suffix.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-wlan-group-my-suffix',
    templateUrl: './wlan-group-my-suffix.component.html'
})
export class WlanGroupMySuffixComponent implements OnInit, OnDestroy {
wlanGroups: WlanGroupMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private wlanGroupService: WlanGroupMySuffixService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.wlanGroupService.query().subscribe(
            (res: ResponseWrapper) => {
                this.wlanGroups = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWlanGroups();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WlanGroupMySuffix) {
        return item.id;
    }
    registerChangeInWlanGroups() {
        this.eventSubscriber = this.eventManager.subscribe('wlanGroupListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
