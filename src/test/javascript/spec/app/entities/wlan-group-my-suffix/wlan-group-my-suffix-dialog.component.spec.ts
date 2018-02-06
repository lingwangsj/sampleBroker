/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanGroupMySuffixDialogComponent } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix-dialog.component';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.service';
import { WlanGroupMySuffix } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.model';
import { WlanMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-my-suffix';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix';

describe('Component Tests', () => {

    describe('WlanGroupMySuffix Management Dialog Component', () => {
        let comp: WlanGroupMySuffixDialogComponent;
        let fixture: ComponentFixture<WlanGroupMySuffixDialogComponent>;
        let service: WlanGroupMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanGroupMySuffixDialogComponent],
                providers: [
                    WlanMySuffixService,
                    ZoneMySuffixService,
                    WlanGroupMySuffixService
                ]
            })
            .overrideTemplate(WlanGroupMySuffixDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanGroupMySuffixDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanGroupMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new WlanGroupMySuffix(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.wlanGroup = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wlanGroupListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new WlanGroupMySuffix();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.wlanGroup = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wlanGroupListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
