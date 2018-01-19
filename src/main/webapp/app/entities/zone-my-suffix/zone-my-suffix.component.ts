import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ZoneMySuffix } from './zone-my-suffix.model';
import { ZoneMySuffixService } from './zone-my-suffix.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-zone-my-suffix',
    templateUrl: './zone-my-suffix.component.html'
})
export class ZoneMySuffixComponent implements OnInit, OnDestroy {
zones: ZoneMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private zoneService: ZoneMySuffixService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.zoneService.query().subscribe(
            (res: ResponseWrapper) => {
                this.zones = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInZones();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ZoneMySuffix) {
        return item.id;
    }
    registerChangeInZones() {
        this.eventSubscriber = this.eventManager.subscribe('zoneListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
