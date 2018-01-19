import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { APMySuffix } from './ap-my-suffix.model';
import { APMySuffixService } from './ap-my-suffix.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-ap-my-suffix',
    templateUrl: './ap-my-suffix.component.html'
})
export class APMySuffixComponent implements OnInit, OnDestroy {
aPS: APMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private aPService: APMySuffixService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.aPService.query().subscribe(
            (res: ResponseWrapper) => {
                this.aPS = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAPS();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: APMySuffix) {
        return item.id;
    }
    registerChangeInAPS() {
        this.eventSubscriber = this.eventManager.subscribe('aPListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
